package bitnagil.bitnagil_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Value("${server.port:unknown}")
    private String port;

    private final Environment environment;

    public HealthCheckController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/health-check/{val}")
    public ResponseEntity<String> health(@PathVariable String val) {
        String activeProfile = String.join(", ", environment.getActiveProfiles());
        // throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.ok("port: " + port + ", active profile: " + activeProfile);
    }
}
