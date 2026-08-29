package bitnagil.onboarding.controller.spec;

import bitnagil.errorcode.ErrorCode;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiErrorCodeExamples;
import bitnagil.global.swagger.ApiTags;
import bitnagil.onboarding.dto.request.OnboardingRequest;
import bitnagil.onboarding.dto.request.OnboardingRequestV2;
import bitnagil.onboarding.dto.request.RegistrationRoutinesRequest;
import bitnagil.onboarding.dto.response.OnboardingResponse;
import bitnagil.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Null;

@Tag(name = ApiTags.ONBOARDING)
public interface OnboardingSpec {

    @Operation(summary = "(v2) 온보딩을 수행하고, 추천 루틴을 응답받습니다. v1과 달리 emotionType 복수선택을 반영하기 위해 배열형태로 받습니다.")
    @ApiErrorCodeExamples({
            ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_RECOMMENDED_ROUTINE, ErrorCode.NOT_FOUND_USER_ONBOARDING_INFO
    })
    public CustomResponseDto<OnboardingResponse> startOnboardingV2(OnboardingRequestV2 onboardingRequestV2, User user);

    @Operation(summary = "온보딩을 수행하고, 추천 루틴을 응답받습니다.")
    @ApiErrorCodeExamples({
            ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_RECOMMENDED_ROUTINE
    })
    public CustomResponseDto<OnboardingResponse> startOnboarding(OnboardingRequest onboardingRequest, User user);

    @Operation(summary = "(V2) 온보딩 시 추천 루틴을 등록합니다.(복수 선택이 가능합니다.)")
    @ApiErrorCodeExamples({
            ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_RECOMMENDED_ROUTINE
    })
    public CustomResponseDto<Object> registrationRoutinesV2(RegistrationRoutinesRequest registrationRoutinesRequest,
                                                        User user);

    @Operation(summary = "온보딩 시 추천 루틴을 등록합니다.(복수 선택이 가능합니다.)")
    @ApiErrorCodeExamples({
            ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_RECOMMENDED_ROUTINE
    })
    public CustomResponseDto<Object> registrationRoutines(RegistrationRoutinesRequest registrationRoutinesRequest,
                                                        User user);
}
