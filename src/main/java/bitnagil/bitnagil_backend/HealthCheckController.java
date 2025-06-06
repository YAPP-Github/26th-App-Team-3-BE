package bitnagil.bitnagil_backend;

import bitnagil.bitnagil_backend.healthCheckSpec.HealthCheckSpec;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController implements HealthCheckSpec { // HealthCheckSpec 인터페이스를 구현하여 Swagger 문서화

    @Value("${server.port:unknown}")
    private String port;

    private final Environment environment;

    public HealthCheckController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/health-check")
    public ResponseEntity<String> health() {
        String activeProfile = String.join(", ", environment.getActiveProfiles());
        return ResponseEntity.ok("port: " + port + ", active profile: " + activeProfile);
    }

    @GetMapping("/health-check/{value}")
    public ResponseEntity<String> health(@PathVariable String value) {
        return ResponseEntity.ok(value);
    }
}
