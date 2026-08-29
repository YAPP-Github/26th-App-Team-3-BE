package bitnagil.report.dto.response;

import java.time.LocalDate;
import java.util.List;

import bitnagil.report.domain.enums.ReportCategory;
import bitnagil.report.domain.enums.ReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReportDetailInfoResponse {

    @Schema(description = "제보 날짜",
        example = "2025-11-16")
    private LocalDate reportDate;

    @Schema(description = "현재 제보 상태",
        example = "IN_PROGRESS")
    private ReportStatus reportStatus;

    @Schema(description = "제보 명",
        example = "가로등 불 꺼짐")
    private String reportTitle;

    @Schema(description = "제보 내용",
        example = "용산역인데 가로등 불이 안들어와요")
    private String reportContent;

    @Schema(description = "제보 카테고리",
        example = "TRANSPORTATION")
    private ReportCategory reportCategory;

    @Schema(description = "제보 위치",
        example = "서울시 강남구 삼성동")
    private String reportLocation;

    @Schema(description = "제보 이미지 URL 리스트",
        example = "[\"https://example.com/report/image1.jpg\", \"https://example.com/report/image2.jpg\"]")
    private List<String> reportImageUrls;
}
