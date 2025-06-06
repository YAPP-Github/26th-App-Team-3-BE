package bitnagil.bitnagil_backend.healthCheckSpec;

import bitnagil.bitnagil_backend.global.ApiTags;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 헬스체크 API 스펙 정의
 */
@Tag(name = ApiTags.HEALTH_CHECK)
public interface HealthCheckSpec {
    @Operation(description = "헬스체크로 포트와 프로파일을 반환합니다.")
    ResponseEntity<String> health();

    @Operation(description = "헬스체크로 전달받은 value를 반환합니다.")
    ResponseEntity<String> health(@PathVariable String value);
}
