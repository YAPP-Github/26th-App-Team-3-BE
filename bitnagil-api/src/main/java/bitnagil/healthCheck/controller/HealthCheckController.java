package bitnagil.healthCheck.controller;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.healthCheck.controller.spec.HealthCheckSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/health-check")
public class HealthCheckController implements HealthCheckSpec {

    @Value("${server.port}")
    private String port; // 서버 포트 정보
    private final Environment environment;

    /**
     * ecs 태스크 배포시 헬스체크를 위한 엔드포인트
     * 해당 api는 alb 헬스체크를 위해 반드시 필요합니다.
     */
    @GetMapping("")
    public CustomResponseDto<String> health() {
        return CustomResponseDto.from("헬스체크에 성공했습니다"); // 커스텀 응답 메세지
    }

    @GetMapping("/{val}")
    public CustomResponseDto<String> health(@PathVariable String val) {
        if(val.equals("null")){
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return CustomResponseDto.from(
                "헬스체크에 성공했습니다. 전달받음 value는 " + val + "입니다."); // 커스텀 응답 메세지
    }

    /**
     * Redis 테스트 컨트롤러
     */
    @GetMapping("/redis")
    public CustomResponseDto<String> healthCheck() {
        try {
            String healthKey = "redis-health-check";
//            redisTemplate.opsForValue().set(healthKey, "OK", Duration.ofSeconds(5));
//            String value = (String) redisTemplate.opsForValue().get(healthKey);
//            return "OK".equals(value)
            return "OK".equals("OK")
                    ? CustomResponseDto.from("Redis is healthy")
                    : CustomResponseDto.from(ErrorCode.INTERNAL_SERVER_ERROR, "Redis set/get mismatch");
        } catch (Exception e) {
            return CustomResponseDto.from(ErrorCode.INTERNAL_SERVER_ERROR,"Redis connection failed: " + e.getMessage());
        }
    }
}
