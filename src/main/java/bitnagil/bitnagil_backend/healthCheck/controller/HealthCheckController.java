package bitnagil.bitnagil_backend.healthCheck.controller;

import bitnagil.bitnagil_backend.global.errorcode.ErrorCode;
import bitnagil.bitnagil_backend.global.exception.CustomException;
import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
import bitnagil.bitnagil_backend.healthCheck.request.HealthCheckRequest;
import bitnagil.bitnagil_backend.healthCheck.controller.spec.HealthCheckSpec;
import bitnagil.bitnagil_backend.healthCheck.response.HealthCheckResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/health-check")
public class HealthCheckController implements HealthCheckSpec {

    @Value("${server.port}")
    private String port; // 서버 포트 정보
    private final RedisTemplate<String, Object> redisTemplate;
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

    @GetMapping("/requset")
    public CustomResponseDto<HealthCheckResponse> health(@RequestBody HealthCheckRequest request) {
        HealthCheckResponse response = HealthCheckResponse.builder()
                .healthCheckId(request.getHealthCheckId() * 3)
                        .title(request.getTitle()).build();
        return CustomResponseDto.from(response);
    }

    /**
     * Redis 테스트 컨트롤러
     */
    @GetMapping("/redis")
    public CustomResponseDto<String> healthCheck() {
        try {
            String healthKey = "redis-health-check";
            redisTemplate.opsForValue().set(healthKey, "OK", Duration.ofSeconds(5));
            String value = (String) redisTemplate.opsForValue().get(healthKey);
            return "OK".equals(value)
                    ? CustomResponseDto.from("Redis is healthy")
                    : CustomResponseDto.from(ErrorCode.INTERNAL_SERVER_ERROR, "Redis set/get mismatch");
        } catch (Exception e) {
            return CustomResponseDto.from(ErrorCode.INTERNAL_SERVER_ERROR,"Redis connection failed: " + e.getMessage());
        }
    }

    @PostMapping("/redis")
    public CustomResponseDto<String> redisDebugFlow(@RequestParam String key, @RequestParam String value) {
        try {
            log.info("🔧 [1] 저장 시도 - key: {}, value: {}", key, value);
            redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(5));
            log.info("✅ [1] 저장 완료");

            log.info("🔍 [2] 조회 시도 - key: {}", key);
            Object fetched = redisTemplate.opsForValue().get(key);
            log.info("📦 [2] 조회 결과 - value: {}", fetched);

            if (fetched == null || !fetched.equals(value)) {
                log.warn("❌ [2] 조회 실패 또는 값 불일치");
                return CustomResponseDto.from(ErrorCode.RESOURCE_NOT_FOUND,"Redis 저장 후 조회 실패 또는 값 불일치");
            }

            log.info("🧹 [3] 삭제 시도 - key: {}", key);
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                log.info("✅ [3] 삭제 완료");
            } else {
                log.warn("⚠️ [3] 삭제 실패");
            }

            return CustomResponseDto.from("✅ Redis 저장/조회/삭제 플로우 완료");

        } catch (Exception e) {
            log.error("🔥 Redis 디버그 중 예외 발생", e);
            return CustomResponseDto.from(ErrorCode.INTERNAL_SERVER_ERROR,"Redis 디버그 중 에러: " + e.getMessage());
        }
    }
}
