package bitnagil.report.dto.response;

import bitnagil.report.domain.enums.ReportCategory;
import bitnagil.report.domain.enums.ReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReportInfo {

    @Schema(description = "제보 ID 식별값입니다.",
            example = "1")
    private Long reportId;

    @Schema(description = "현재 제보 상태",
            example = "IN_PROGRESS")
    private ReportStatus reportStatus;

    @Schema(description = "제보 명",
            example = "가로등 불 꺼짐")
    private String reportTitle;

    @Schema(description = "제보 카테고리",
            example = "TRANSPORTATION")
    private ReportCategory reportCategory;

    @Schema(description = "제보 위치",
            example = "서울시 강남구 삼성동")
    private String reportLocation;

    @Schema(description = "제보 이미지 URL 리스트",
            example = "https://example.com/report/image1.jpg")
    private String reportImageUrl;
}
