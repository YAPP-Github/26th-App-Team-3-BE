package bitnagil.bitnagil_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/new")
    public String newURI() {
        return "Github Actions로 배포 성공";
    }
}
