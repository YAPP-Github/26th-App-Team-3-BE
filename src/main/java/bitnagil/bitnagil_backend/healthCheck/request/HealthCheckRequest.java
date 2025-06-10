package bitnagil.bitnagil_backend.healthCheck.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 헬스체크 응답 DTO
 * Swagger에 문서화를 위해 @Schema 어노테이션을 사용합니다.
 */
@Getter
@AllArgsConstructor
@Schema(description = "헬스체크 Request DTO")
public class HealthCheckRequest {

    @Schema(description = "헬스체크 아이디", example = "1")
    @NotNull
    private Long healthCheckId;

    @Schema(description = "타이틀", example = "테스트")
    private String title;

}

