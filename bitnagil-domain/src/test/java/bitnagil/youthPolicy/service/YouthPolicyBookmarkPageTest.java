package bitnagil.youthPolicy.service;

import bitnagil.youthPolicy.dto.BookmarkRef;
import bitnagil.youthPolicy.dto.PeriodType;
import bitnagil.youthPolicy.dto.PolicyStatus;
import bitnagil.youthPolicy.dto.RankedYouthPolicy;
import bitnagil.youthPolicy.dto.YouthPolicyDoc;
import bitnagil.youthPolicy.dto.YouthPolicyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 찜 목록 조회(getBookmarkedPage) 순수 로직 테스트.
 */
class YouthPolicyBookmarkPageTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 7, 19);

    private final YouthPolicyQueryService queryService = new YouthPolicyQueryService(new YouthPolicyCursor());

    @Test
    @DisplayName("찜 목록은 최근 찜한 순(bookmarkId 내림차순)으로 반환된다")
    void ordersByMostRecentlyBookmarked() {
        // bookmarkId가 클수록 최근에 찜한 것
        List<BookmarkRef> bookmarks = List.of(
            new BookmarkRef(1L, "P1"),
            new BookmarkRef(3L, "P3"),
            new BookmarkRef(2L, "P2"));
        Map<String, YouthPolicyDoc> docs = docMap(
            openDoc("P1", LocalDate.of(2026, 8, 1)),
            openDoc("P2", LocalDate.of(2026, 7, 25)),
            openDoc("P3", LocalDate.of(2026, 9, 1)));

        YouthPolicyPage page = queryService.getBookmarkedPage(bookmarks, docs, null, 50, TODAY);

        // 마감일과 무관하게 bookmarkId 내림차순: 3 → 2 → 1
        assertThat(plcyNos(page)).containsExactly("P3", "P2", "P1");
        assertThat(page.totalCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("마감된 찜 공고도 목록에 포함된다")
    void includesClosedBookmarks() {
        List<BookmarkRef> bookmarks = List.of(
            new BookmarkRef(2L, "OPEN"),
            new BookmarkRef(1L, "CLOSED"));
        Map<String, YouthPolicyDoc> docs = docMap(
            openDoc("OPEN", LocalDate.of(2026, 8, 1)),
            closedDoc("CLOSED"));

        YouthPolicyPage page = queryService.getBookmarkedPage(bookmarks, docs, null, 50, TODAY);

        assertThat(plcyNos(page)).containsExactly("OPEN", "CLOSED");
        assertThat(statusOf(page, "CLOSED")).isEqualTo(PolicyStatus.CLOSED);
    }

    @Test
    @DisplayName("캐시에 없는(삭제된) 찜 공고는 건너뛴다")
    void skipsPoliciesMissingFromCache() {
        List<BookmarkRef> bookmarks = List.of(
            new BookmarkRef(2L, "EXISTS"),
            new BookmarkRef(1L, "GONE")); // 캐시에 없음
        Map<String, YouthPolicyDoc> docs = docMap(openDoc("EXISTS", LocalDate.of(2026, 8, 1)));

        YouthPolicyPage page = queryService.getBookmarkedPage(bookmarks, docs, null, 50, TODAY);

        assertThat(plcyNos(page)).containsExactly("EXISTS");
        assertThat(page.totalCount()).isEqualTo(1); // GONE은 카운트에서도 제외
    }

    @Test
    @DisplayName("bookmarkId 커서로 끝까지 페이징하면 중복·누락 없이 최근순으로 조회된다")
    void paginatesByBookmarkIdCursor() {
        List<BookmarkRef> bookmarks = new ArrayList<>();
        List<YouthPolicyDoc> docList = new ArrayList<>();
        for (long id = 1; id <= 5; id++) {
            bookmarks.add(new BookmarkRef(id, "P" + id));
            docList.add(openDoc("P" + id, LocalDate.of(2026, 8, 1)));
        }
        Map<String, YouthPolicyDoc> docs = docMap(docList.toArray(new YouthPolicyDoc[0]));
        int size = 2;

        List<String> collected = new ArrayList<>();
        String cursor = null;
        int guard = 0;
        while (true) {
            YouthPolicyPage page = queryService.getBookmarkedPage(bookmarks, docs, cursor, size, TODAY);
            collected.addAll(plcyNos(page));
            assertThat(page.items().size()).isLessThanOrEqualTo(size);
            if (!page.hasNext()) {
                assertThat(page.nextCursor()).isNull();
                break;
            }
            cursor = page.nextCursor();
            assertThat(cursor).isNotNull();
            if (++guard > 100) {
                throw new IllegalStateException("페이징이 끝나지 않음");
            }
        }

        // 최근순(내림차순): P5, P4, P3, P2, P1
        assertThat(collected).containsExactly("P5", "P4", "P3", "P2", "P1");
        assertThat(collected).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("nextCursor는 인코딩 없이 마지막 항목의 bookmarkId 문자열이다")
    void nextCursorIsPlainBookmarkId() {
        List<BookmarkRef> bookmarks = List.of(
            new BookmarkRef(10L, "P10"),
            new BookmarkRef(9L, "P9"),
            new BookmarkRef(8L, "P8"));
        Map<String, YouthPolicyDoc> docs = docMap(
            openDoc("P10", LocalDate.of(2026, 8, 1)),
            openDoc("P9", LocalDate.of(2026, 8, 1)),
            openDoc("P8", LocalDate.of(2026, 8, 1)));

        YouthPolicyPage page = queryService.getBookmarkedPage(bookmarks, docs, null, 2, TODAY);

        // 첫 페이지 [P10, P9], 다음 커서는 마지막 항목 bookmarkId "9"
        assertThat(plcyNos(page)).containsExactly("P10", "P9");
        assertThat(page.hasNext()).isTrue();
        assertThat(page.nextCursor()).isEqualTo("9");
    }

    private List<String> plcyNos(YouthPolicyPage page) {
        return page.items().stream().map(RankedYouthPolicy::doc).map(YouthPolicyDoc::getPlcyNo).toList();
    }

    private PolicyStatus statusOf(YouthPolicyPage page, String plcyNo) {
        return page.items().stream()
            .filter(r -> r.doc().getPlcyNo().equals(plcyNo))
            .findFirst().orElseThrow().status();
    }

    private Map<String, YouthPolicyDoc> docMap(YouthPolicyDoc... docs) {
        Map<String, YouthPolicyDoc> map = new LinkedHashMap<>();
        for (YouthPolicyDoc doc : docs) {
            map.put(doc.getPlcyNo(), doc);
        }
        return map;
    }

    private YouthPolicyDoc openDoc(String plcyNo, LocalDate endDate) {
        return YouthPolicyDoc.builder()
            .plcyNo(plcyNo).title("공고-" + plcyNo).category("일자리")
            .periodType(PeriodType.FIXED).startDate(endDate.minusDays(10)).endDate(endDate)
            .zipCodes(List.of("11110")).applyUrl(null).build();
    }

    private YouthPolicyDoc closedDoc(String plcyNo) {
        return YouthPolicyDoc.builder()
            .plcyNo(plcyNo).title("공고-" + plcyNo).category("일자리")
            .periodType(PeriodType.CLOSED).startDate(null).endDate(null)
            .zipCodes(List.of("11110")).applyUrl(null).build();
    }
}