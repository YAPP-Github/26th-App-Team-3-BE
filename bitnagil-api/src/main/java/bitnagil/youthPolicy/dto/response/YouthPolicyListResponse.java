package bitnagil.youthPolicy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 공고 목록 조회 응답입니다. 커서 기반 무한 스크롤을 지원합니다.
 */
@Getter
@Builder
public class YouthPolicyListResponse {

    @Schema(description = "필터링된 전체 건수(탭 뱃지용)", example = "47")
    private final int totalCount;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private final boolean hasNext;

    @Schema(description = "다음 페이지 커서(없으면 null)", nullable = true)
    private final String nextCursor;

    @Schema(description = "공고 카드 목록")
    private final List<YouthPolicyCardResponse> items;
}