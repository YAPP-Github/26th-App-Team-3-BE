package bitnagil.bitnagil_backend.healthCheck.controller.spec;

import bitnagil.bitnagil_backend.global.swagger.ApiTags;
import bitnagil.bitnagil_backend.global.swagger.ApiErrorCodeExample;
import bitnagil.bitnagil_backend.global.errorcode.ErrorCode;
import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
import bitnagil.bitnagil_backend.global.swagger.ApiErrorCodeExamples;
import bitnagil.bitnagil_backend.healthCheck.request.HealthCheckRequest;
import bitnagil.bitnagil_backend.healthCheck.response.HealthCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 헬스체크 API 스펙 정의
 */
@Tag(name = ApiTags.HEALTH_CHECK)
public interface HealthCheckSpec {
    @Operation(summary = "헬스체크를 수행합니다.")
    CustomResponseDto<String> health();

    @Operation(summary = "헬스체크로 전달받은 value를 반환합니다.")
    @ApiErrorCodeExample(ErrorCode.RESOURCE_NOT_FOUND)
    // PathVariable 파라미터 설명
    @Parameters({
            @Parameter(name = "val", description = "value 값", required = true, example = "healthCheckValue")
    })
    CustomResponseDto<String> health(@PathVariable String val);

    @Operation(summary = "헬스체크로 요청 본문을 반환합니다.")
    CustomResponseDto<HealthCheckResponse> health(HealthCheckRequest request);

    @Operation(summary = "Redis 헬스체크 API입니다. Redis 연결 상태를 확인합니다.")
    @ApiErrorCodeExample(ErrorCode.INTERNAL_SERVER_ERROR)
    CustomResponseDto<String> healthCheck();

    @Operation(summary = "Redis 디버그 플로우 API입니다. Redis에 key-value를 저장하고 확인합니다.")
    @ApiErrorCodeExamples({ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.RESOURCE_NOT_FOUND})
    // RequestParam 파라미터 설명
    @Parameters({
            @Parameter(name = "key", description = "redis key", required = true, example = "testKey"),
            @Parameter(name = "value", description = "redis value", required = false, example = "testValue")
    })
    CustomResponseDto<String> redisDebugFlow(@RequestParam String key, @RequestParam String value);
}

