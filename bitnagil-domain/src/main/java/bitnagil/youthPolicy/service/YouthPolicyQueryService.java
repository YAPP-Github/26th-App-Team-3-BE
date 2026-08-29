package bitnagil.youthPolicy.service;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.youthPolicy.dto.BookmarkRef;
import bitnagil.youthPolicy.dto.PeriodType;
import bitnagil.youthPolicy.dto.PolicyStatus;
import bitnagil.youthPolicy.dto.RankedYouthPolicy;
import bitnagil.youthPolicy.dto.YouthPolicyDoc;
import bitnagil.youthPolicy.dto.YouthPolicyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 청년 공고 조회의 순수 로직입니다.
 * 전체 공고를 상태 계산 → 마감 제외 → (내 지역 우선, 마감임박순) 정렬 → 커서 슬라이스합니다.
 * 지역은 필터가 아니라 정렬 우선순위로만 반영합니다(내 지역 공고가 앞, 나머지가 뒤).
 */
@Service
@RequiredArgsConstructor
public class YouthPolicyQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 50;

    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyyMMdd");
    // 지역 티어(1자리). 내 지역 일치=0(앞), 불일치=1(뒤).
    private static final String REGION_MATCH = "0";
    private static final String REGION_OTHER = "1";
    // 정렬키(8자리). OPEN은 마감일(yyyyMMdd), 그 뒤로 상시 → 마감 순.
    private static final String SORT_KEY_OPEN_NO_DATE = "99999997";
    private static final String SORT_KEY_ALWAYS = "99999998";
    private static final String SORT_KEY_CLOSED = "99999999";
    private static final String CURSOR_KEY_DELIMITER = "|";

    private final YouthPolicyCursor youthPolicyCursor;

    /**
     * 전체 공고에서 마감을 제외하고 내 지역 우선·마감임박순으로 정렬한 뒤 커서로 한 페이지를 잘라 반환합니다.
     */
    public YouthPolicyPage getPage(List<YouthPolicyDoc> allDocs, String sigunguCode,
                                   String cursor, Integer size, LocalDate today) {
        int pageSize = clampSize(size); // 페이징 적용 사이즈 설정

        // 상태 계산 → 마감 제외 → 커서키 계산(항목당 1회) → 커서키 사전순 정렬
        List<Keyed> ranked = allDocs.stream()
            .map(doc -> new RankedYouthPolicy(doc, resolveStatus(doc, today))) // 상태 확인하여 랭킹 모델로 변환
            .filter(r -> r.status() != PolicyStatus.CLOSED) // 마감 제외
            .map(r -> new Keyed(cursorKey(r, sigunguCode), r)) // 시군구코드 기반 커서키 계산
            .sorted(Comparator.comparing(Keyed::key)) // 커서키 사전순 정렬
            .toList();

        int totalCount = ranked.size();

        // 커서 적용 후 페이지 사이즈만큼 잘라 반환
        List<Keyed> afterCursor = applyCursor(ranked, cursor);
        boolean hasNext = afterCursor.size() > pageSize;
        List<Keyed> page = afterCursor.stream().limit(pageSize).toList();
        String nextCursor = hasNext
            ? youthPolicyCursor.encode(page.get(page.size() - 1).key()) // 커서는 평문으로 전달하지 않고 인코딩하여 전달한다.
            : null;

        List<RankedYouthPolicy> items = page.stream().map(Keyed::ranked).toList();
        return new YouthPolicyPage(items, totalCount, hasNext, nextCursor);
    }

    /**
     * 찜한 공고를 최근 찜한 순으로 반환합니다. 마감 공고도 포함하며 지역은 보지 않습니다.
     * 정렬 기준이 공고 속성이 아니라 찜 시점(bookmarkId)이라 전체 탭 로직과 분리되어 있습니다.
     *
     * @param orderedBookmarks 최근순(bookmarkId 내림차순)으로 정렬된 찜 목록
     * @param docByPlcyNo      캐시된 공고를 plcyNo로 조회하는 맵
     */
    public YouthPolicyPage getBookmarkedPage(List<BookmarkRef> orderedBookmarks,
                                             Map<String, YouthPolicyDoc> docByPlcyNo,
                                             String cursor, Integer size, LocalDate today) {
        int pageSize = clampSize(size);

        // 최근순 유지하며 캐시에서 hydrate. 캐시에 없으면(업스트림에서 삭제) 스킵.
        List<BookmarkKeyed> hydrated = orderedBookmarks.stream()
            .map(ref -> hydrate(ref, docByPlcyNo, today))
            .filter(Objects::nonNull)
            .toList();

        int totalCount = hydrated.size();

        // bookmarkId 커서: 최근순(내림차순)이므로 커서보다 작은 bookmarkId만 남긴다.
        // bookmarkId는 그 자체로 불투명할 게 없는 숫자라 인코딩 없이 문자열로 주고받는다.
        Long startBookmarkId = parseBookmarkCursor(cursor);
        List<BookmarkKeyed> afterCursor = startBookmarkId == null ? hydrated
            : hydrated.stream().filter(h -> h.bookmarkId() < startBookmarkId).toList();

        boolean hasNext = afterCursor.size() > pageSize;
        List<BookmarkKeyed> page = afterCursor.stream().limit(pageSize).toList();
        String nextCursor = hasNext
            ? String.valueOf(page.get(page.size() - 1).bookmarkId())
            : null;

        List<RankedYouthPolicy> items = page.stream().map(BookmarkKeyed::ranked).toList();
        return new YouthPolicyPage(items, totalCount, hasNext, nextCursor);
    }

    private BookmarkKeyed hydrate(BookmarkRef ref, Map<String, YouthPolicyDoc> docByPlcyNo, LocalDate today) {
        YouthPolicyDoc doc = docByPlcyNo.get(ref.plcyNo());
        if (doc == null) {
            return null;
        }
        return new BookmarkKeyed(ref.bookmarkId(), new RankedYouthPolicy(doc, resolveStatus(doc, today)));
    }

    private Long parseBookmarkCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(cursor);
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.INVALID_YOUTH_POLICY_CURSOR);
        }
    }

    /**
     * 조회 시점(today) 기준으로 노출 상태를 계산합니다.
     */
    public PolicyStatus resolveStatus(YouthPolicyDoc doc, LocalDate today) {
        if (doc.getPeriodType() == PeriodType.ALWAYS) {
            return PolicyStatus.ALWAYS;
        }
        if (doc.getPeriodType() == PeriodType.CLOSED) {
            return PolicyStatus.CLOSED;
        }
        // FIXED: 마감일이 지났으면 CLOSED (업스트림이 마감 처리를 안 한 경우 방어)
        if (doc.getEndDate() != null && doc.getEndDate().isBefore(today)) {
            return PolicyStatus.CLOSED;
        }
        return PolicyStatus.OPEN;
    }

    private List<Keyed> applyCursor(List<Keyed> ranked, String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return ranked;
        }
        String startKey = youthPolicyCursor.decode(cursor); // 커서 키를 평문으로 디코딩
        return ranked.stream()
            .filter(keyed -> keyed.key().compareTo(startKey) > 0) // 커서보다 사전순으로 뒤에 오는 항목만
            .toList();
    }

    // 커서 복합 키. "지역 우선순위 + 정렬키 + | + plcyNo" ( 사전순 = 우선순위순).
    private String cursorKey(RankedYouthPolicy ranked, String sigunguCode) {
        return regionTier(ranked, sigunguCode) + sortKey(ranked) + CURSOR_KEY_DELIMITER + ranked.doc().getPlcyNo();
    }

    // 내 지역 우선순위 계산. 내 지역이면 0, 아니면 1.
    private String regionTier(RankedYouthPolicy ranked, String sigunguCode) {
        return ranked.doc().getZipCodes().contains(sigunguCode) ? REGION_MATCH : REGION_OTHER;
    }

    private String sortKey(RankedYouthPolicy ranked) {
        return switch (ranked.status()) {
            case OPEN -> ranked.doc().getEndDate() != null
                ? ranked.doc().getEndDate().format(YMD)
                : SORT_KEY_OPEN_NO_DATE;
            case ALWAYS -> SORT_KEY_ALWAYS;
            case CLOSED -> SORT_KEY_CLOSED;
        };
    }

    // 사이즈 설정
    // size가 null이면 DEFAULT_PAGE_SIZE, 50 초과시 50
    private int clampSize(Integer size) {
        if (size == null) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.max(1, Math.min(size, MAX_PAGE_SIZE));
    }

    // 커서키를 항목당 한 번만 계산해 정렬·페이징에 재사용하기 위한 내부 DTO (전체 탭)
    private record Keyed(String key, RankedYouthPolicy ranked) {
    }

    // 찜 탭 커서(bookmarkId)와 hydrate된 공고를 함께 다루기 위한 내부 DTO
    private record BookmarkKeyed(Long bookmarkId, RankedYouthPolicy ranked) {
    }
}