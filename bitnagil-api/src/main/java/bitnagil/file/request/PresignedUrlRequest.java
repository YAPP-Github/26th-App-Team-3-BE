package bitnagil.file.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Presigned URL 발급 요청 DTO")
public class PresignedUrlRequest {

    @Schema(description = "파일명을 제외한 접두사",
            example = "report를 prefix로 사용할 경우 실제 S3에 저장되는 경로는 report/image.png가 됩니다.")
    private String prefix;

    @Schema(description = "파일명",
            example = "image.png",
            required = true)
    @NotNull
    private String fileName;
}
