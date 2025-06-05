package bitnagil.bitnagil_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Value("${spring.port}")
    private String port;

    @Value("${spring.config.active.on-profile}")
    private String activeProfile;


    @GetMapping("/health-check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("pot: " + port + ", active profile: " + activeProfile);
    }
}
