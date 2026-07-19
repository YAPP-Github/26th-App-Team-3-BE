package bitnagil.youthPolicy.service;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.infrastructure.vworld.GeocodingService;
import bitnagil.user.domain.User;
import bitnagil.youthPolicy.dto.BookmarkRef;
import bitnagil.youthPolicy.dto.YouthPolicyDoc;
import bitnagil.youthPolicy.dto.YouthPolicyPage;
import bitnagil.youthPolicy.dto.response.YouthPolicyCardResponse;
import bitnagil.youthPolicy.dto.response.YouthPolicyListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 청년 공고 조회 파사드입니다.
 */
@Service
@RequiredArgsConstructor
public class YouthPolicyService {

    private final GeocodingService geocodingService;
    private final YouthPolicyCacheService youthPolicyCacheService;
    private final YouthPolicyQueryService youthPolicyQueryService;
    private final YouthPolicyBookmarkService youthPolicyBookmarkService;
    private final YouthPolicyMapper youthPolicyMapper;

    // 청년 공고 목록 조회
    public YouthPolicyListResponse getYouthPolicies(User user, double latitude, double longitude,
                                                    String cursor, Integer size) {
        // 시군구코드 변환
        String sigunguCode = geocodingService.resolveSigunguCode(latitude, longitude)
            .orElseThrow(() -> new CustomException(ErrorCode.LOCATION_NOT_RESOLVED));

        LocalDate today = LocalDate.now();

        // Redis 캐시에서 모든 공고 조회 후, 시군구코드 기준으로 필터링 및 페이징 처리
        List<YouthPolicyDoc> allDocs = youthPolicyCacheService.getAll();
        YouthPolicyPage page = youthPolicyQueryService.getPage(allDocs, sigunguCode, cursor, size, today);

        // 사용자가 북마크한 공고 번호 조회
        Set<String> bookmarkedPlcyNos = youthPolicyBookmarkService.findBookmarkedPlcyNos(user);
        List<YouthPolicyCardResponse> items = page.items().stream()
            .map(ranked -> youthPolicyMapper.toCard(
                ranked, bookmarkedPlcyNos.contains(ranked.doc().getPlcyNo()), today))
            .toList();

        return toListResponse(page, items);
    }

    // 찜한 공고 목록 조회 (최근 찜한 순, 지역 무관)
    public YouthPolicyListResponse getBookmarkedYouthPolicies(User user, String cursor, Integer size) {
        List<BookmarkRef> recentBookmarks = youthPolicyBookmarkService.findRecentBookmarks(user);
        if (recentBookmarks.isEmpty()) {
            return emptyListResponse(); // 찜 0건 → 캐시조차 건드리지 않고 빈 페이지
        }

        LocalDate today = LocalDate.now();

        // 캐시된 전체 공고를 plcyNo로 조회할 수 있게 맵으로 만든다
        Map<String, YouthPolicyDoc> docByPlcyNo = youthPolicyCacheService.getAll().stream()
            .collect(Collectors.toMap(YouthPolicyDoc::getPlcyNo, Function.identity(), (a, b) -> a));

        // 최근 찜한 순으로 정렬된 북마크 목록을 기반으로 커서 페이징 처리
        YouthPolicyPage page = youthPolicyQueryService.getBookmarkedPage(recentBookmarks, docByPlcyNo, cursor, size, today);
        List<YouthPolicyCardResponse> items = page.items().stream()
            .map(ranked -> youthPolicyMapper.toCard(ranked, true, today)) // 찜 탭은 항상 bookmarked=true
            .toList();

        return toListResponse(page, items);
    }

    // 찜 등록 (멱등)
    public void bookmark(User user, String plcyNo) {
        youthPolicyBookmarkService.bookmark(user, plcyNo);
    }

    // 찜 해제 (멱등)
    public void unbookmark(User user, String plcyNo) {
        youthPolicyBookmarkService.unbookmark(user, plcyNo);
    }

    private YouthPolicyListResponse toListResponse(YouthPolicyPage page, List<YouthPolicyCardResponse> items) {
        return YouthPolicyListResponse.builder()
            .totalCount(page.totalCount())
            .hasNext(page.hasNext())
            .nextCursor(page.nextCursor())
            .items(items)
            .build();
    }

    private YouthPolicyListResponse emptyListResponse() {
        return YouthPolicyListResponse.builder()
            .totalCount(0)
            .hasNext(false)
            .nextCursor(null)
            .items(List.of())
            .build();
    }
}