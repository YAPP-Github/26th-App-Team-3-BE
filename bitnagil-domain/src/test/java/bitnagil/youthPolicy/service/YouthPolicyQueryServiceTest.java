package bitnagil.youthPolicy.service;

import bitnagil.youthPolicy.dto.PeriodType;
import bitnagil.youthPolicy.dto.RankedYouthPolicy;
import bitnagil.youthPolicy.dto.YouthPolicyDoc;
import bitnagil.youthPolicy.dto.YouthPolicyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class YouthPolicyQueryServiceTest {

    private static final String MY_REGION = "11110";     // 내 시군구
    private static final String OTHER_REGION = "26110";  // 타 시군구
    private static final LocalDate TODAY = LocalDate.of(2026, 7, 18);

    private final YouthPolicyQueryService queryService = new YouthPolicyQueryService(new YouthPolicyCursor());

    // 시나리오 공고들 (plcyNo로 식별)
    private YouthPolicyDoc myOpenSoon() {   // 내 지역, 마감 임박(07-25)
        return doc("B", PeriodType.FIXED, LocalDate.of(2026, 7, 25), MY_REGION);
    }

    private YouthPolicyDoc myOpenLater() {  // 내 지역, 마감 늦음(08-01)
        return doc("A", PeriodType.FIXED, LocalDate.of(2026, 8, 1), MY_REGION);
    }

    private YouthPolicyDoc myAlways() {     // 내 지역, 상시
        return doc("C", PeriodType.ALWAYS, null, MY_REGION);
    }

    private YouthPolicyDoc otherOpen() {    // 타 지역, 마감 임박(07-25)
        return doc("F", PeriodType.FIXED, LocalDate.of(2026, 7, 25), OTHER_REGION);
    }

    private YouthPolicyDoc otherAlways() {  // 타 지역, 상시
        return doc("G", PeriodType.ALWAYS, null, OTHER_REGION);
    }

    private YouthPolicyDoc fixedExpired() { // 마감일 지남(07-01) → CLOSED
        return doc("D", PeriodType.FIXED, LocalDate.of(2026, 7, 1), MY_REGION);
    }

    private YouthPolicyDoc closed() {       // 마감 코드 → CLOSED
        return doc("E", PeriodType.CLOSED, null, MY_REGION);
    }

    @Test
    @DisplayName("내 지역이 먼저, 각 그룹 안에서 마감임박순 → 상시 순으로 정렬된다")
    void ordersRegionFirstThenByDeadline() {
        List<YouthPolicyDoc> all = List.of(
            otherAlways(), otherOpen(), myAlways(), myOpenLater(), myOpenSoon());

        YouthPolicyPage page = queryService.getPage(all, MY_REGION, null, 50, TODAY);

        // 내 지역(B 07-25, A 08-01, C 상시) → 타 지역(F 07-25, G 상시)
        assertThat(plcyNos(page)).containsExactly("B", "A", "C", "F", "G");
    }

    @Test
    @DisplayName("마감(코드 마감 + 마감일 지난 FIXED)은 제외되고 totalCount에도 안 잡힌다")
    void excludesClosed() {
        List<YouthPolicyDoc> all = List.of(myOpenSoon(), fixedExpired(), closed(), myAlways());

        YouthPolicyPage page = queryService.getPage(all, MY_REGION, null, 50, TODAY);

        assertThat(plcyNos(page)).containsExactly("B", "C");
        assertThat(page.totalCount()).isEqualTo(2);
        assertThat(page.hasNext()).isFalse();
        assertThat(page.nextCursor()).isNull();
    }

    @Test
    @DisplayName("커서로 끝까지 페이징하면 중복·누락 없이 전체를 순서대로 조회한다")
    void paginatesWithoutDuplicatesOrGaps() {
        List<YouthPolicyDoc> all = List.of(
            otherAlways(), otherOpen(), myAlways(), myOpenLater(), myOpenSoon());
        int size = 2;

        List<String> collected = new ArrayList<>();
        String cursor = null;
        int totalCount = -1;
        int guard = 0;
        while (true) {
            YouthPolicyPage page = queryService.getPage(all, MY_REGION, cursor, size, TODAY);
            totalCount = page.totalCount();
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

        assertThat(collected).containsExactly("B", "A", "C", "F", "G");
        assertThat(collected).doesNotHaveDuplicates();
        assertThat(collected).hasSize(totalCount);
    }

    @Test
    @DisplayName("지역 결과가 없어도(내 지역 공고 0건) 타 지역 공고는 조회된다")
    void otherRegionShownWhenNoLocalMatch() {
        List<YouthPolicyDoc> all = List.of(otherOpen(), otherAlways());

        YouthPolicyPage page = queryService.getPage(all, MY_REGION, null, 50, TODAY);

        assertThat(plcyNos(page)).containsExactly("F", "G");
    }

    private List<String> plcyNos(YouthPolicyPage page) {
        return page.items().stream().map(RankedYouthPolicy::doc).map(YouthPolicyDoc::getPlcyNo).toList();
    }

    private YouthPolicyDoc doc(String plcyNo, PeriodType type, LocalDate endDate, String zipCode) {
        return YouthPolicyDoc.builder()
            .plcyNo(plcyNo)
            .title("공고-" + plcyNo)
            .category("일자리")
            .periodType(type)
            .startDate(endDate == null ? null : endDate.minusDays(10))
            .endDate(endDate)
            .zipCodes(List.of(zipCode))
            .applyUrl(null)
            .build();
    }
}