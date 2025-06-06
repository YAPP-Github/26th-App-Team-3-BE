package bitnagil.bitnagil_backend;

import bitnagil.bitnagil_backend.global.errorcode.CommonErrorCode;
import bitnagil.bitnagil_backend.global.exception.CustomException;
import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
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
    public CustomResponseDto<String> health(@PathVariable String val) {
        String activeProfile = String.join(", ", environment.getActiveProfiles());
        // throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR); // 예외 처리
        //return CustomResponseDto.from("헬스체크에 성공했습니다. "); // 기본 응답 메세지
        return CustomResponseDto.from(CommonErrorCode.OK,
                "헬스체크에 성공했습니다. 현재 활성화된 프로필: " + activeProfile + ", 서버 포트: " + port); // 커스텀 응답 메세지
    }
}
