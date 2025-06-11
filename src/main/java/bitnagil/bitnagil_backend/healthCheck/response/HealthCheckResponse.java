package bitnagil.bitnagil_backend.healthCheck.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 헬스체크 응답 DTO
 * Swagger에 문서화를 위해 @Schema 어노테이션을 사용합니다.
 */
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "헬스체크 Response DTO")
public class HealthCheckResponse {

    @Schema(description = "헬스체크 아이디 결과", example = "4")
    @NotNull
    private Long healthCheckId;

    @Schema(description = "타이틀 결과", example = "테스트테스트테스트테스트")
    private String title;

    public static HealthCheckResponse of(Long healthCheckId, String title) {
        return HealthCheckResponse.builder()
                .healthCheckId(healthCheckId)
                .title(title)
                .build();
    }

}

