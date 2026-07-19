package bitnagil.youthPolicy.dto.response;

import bitnagil.youthPolicy.dto.PolicyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 공고 목록의 개별 카드 응답입니다.
 */
@Getter
@Builder
public class YouthPolicyCardResponse {

    @Schema(description = "공고 번호(고유 ID)", example = "20260625005400113245")
    private final String plcyNo;

    @Schema(description = "공고 제목", example = "사회연대경제 청년일경험")
    private final String title;

    @Schema(description = "대분류(nullable)", example = "일자리")
    private final String category;

    @Schema(description = "썸네일 이미지 URL (현재 항상 null)", nullable = true)
    private final String thumbnailUrl;

    @Schema(description = "노출 상태", example = "OPEN")
    private final PolicyStatus status;

    @Schema(description = "신청 시작일(nullable)", example = "2026-06-01", nullable = true)
    private final LocalDate startDate;

    @Schema(description = "신청 마감일(상시면 null)", example = "2026-09-30", nullable = true)
    private final LocalDate endDate;

    @Schema(description = "마감까지 남은 일수(OPEN이 아니면 null)", example = "82", nullable = true)
    private final Integer dday;

    @Schema(description = "신청 페이지 URL(nullable)", nullable = true)
    private final String applyUrl;

    @Schema(description = "현재 사용자의 찜 여부", example = "false")
    private final boolean bookmarked;
}