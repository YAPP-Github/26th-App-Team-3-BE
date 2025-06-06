package bitnagil.bitnagil_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

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
}
