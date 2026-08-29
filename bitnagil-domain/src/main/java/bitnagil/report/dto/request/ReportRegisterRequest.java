package bitnagil.report.dto.request;

import bitnagil.report.domain.enums.ReportCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "제보 등록 요청 DTO")
public class ReportRegisterRequest {

    @Schema(description = "제보 명",
            example = "가로등 불 꺼짐",
            required = true)
    @NotNull
    private String reportTitle;

    @Schema(description = "제보 내용",
            example = "용산역인데 가로등 불이 안들어와요")
    private String reportContent;

    @Schema(description = "제보 카테고리",
            example = "TRANSPORTATION",
            required = true)
    @NotNull
    private ReportCategory reportCategory;

    @Schema(description = "제보 이미지 URL 리스트",
            example = "[\"https://example.com/report/image1.jpg\", \"https://example.com/report/image2.jpg\"]" + "\n"
    +"S3에 업로드 시 url에 https://example-bucket.s3.amazonaws.com/report/2d84%A4%EC%9D%BC.jpg?x-amz-acl=... 와 같은 쿼리파아미터가 붙어있는데, 이 쿼리파아미터는 모두 잘라서 앞부분에 해당하는 url만 요청해주세요.")
    private List<String> reportImageUrls;

    @Schema(description = "제보 위치",
            example = "서울시 강남구 삼성동",
            required = true)
    @NotNull
    private String reportLocation;

    @Schema(description = "위도",
            example = "37.5642135",
            required = true)
    @NotNull
    private BigDecimal latitude;

    @Schema(description = "경도",
            example = "126.9780685",
            required = true)
    @NotNull
    private BigDecimal longitude;
}
