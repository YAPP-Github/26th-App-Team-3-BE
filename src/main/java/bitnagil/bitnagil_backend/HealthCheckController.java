package bitnagil.bitnagil_backend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "헬스체크 API", description = "헬스체크를 확인합니다.")
public class HealthCheckController {

    @Value("${server.port:unknown}")
    private String port;

    private final Environment environment;

    public HealthCheckController(Environment environment) {
        this.environment = environment;
    }

    @Operation(summary = "포트와 프로파일 확인", description = "헬스체크로 포트와 프로파일을 반환합니다.")
    @GetMapping("/health-check")
    public ResponseEntity<String> health() {
        String activeProfile = String.join(", ", environment.getActiveProfiles());
        return ResponseEntity.ok("port: " + port + ", active profile: " + activeProfile);
    }

    @Operation(summary = "value 확인", description = "헬스체크로 전달받은 value를 반환합니다.")
    @GetMapping("/health-check/{value}")
    public ResponseEntity<String> health(@PathVariable String value) {
        return ResponseEntity.ok(value);
    }
}
