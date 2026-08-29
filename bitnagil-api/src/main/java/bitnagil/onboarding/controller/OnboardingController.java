package bitnagil.onboarding.controller;

import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.onboarding.controller.spec.OnboardingSpec;
import bitnagil.onboarding.dto.request.OnboardingRequest;
import bitnagil.onboarding.dto.request.OnboardingRequestV2;
import bitnagil.onboarding.dto.request.RegistrationRoutinesRequest;
import bitnagil.onboarding.dto.response.OnboardingResponse;
import bitnagil.onboarding.service.OnboardingService;
import bitnagil.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class OnboardingController implements OnboardingSpec {

    private final OnboardingService onboardingService;

    @Deprecated
    @PostMapping("/v1/onboardings")
    public CustomResponseDto<OnboardingResponse> startOnboarding(@RequestBody OnboardingRequest onboardingRequest,
                                                                @CurrentUser User user) {
        return CustomResponseDto.from(onboardingService.startOnboarding(onboardingRequest, user));
    }

    @PostMapping("/v2/onboardings")
    public CustomResponseDto<OnboardingResponse> startOnboardingV2(@RequestBody OnboardingRequestV2 onboardingRequest,
                                                                 @CurrentUser User user) {
        return CustomResponseDto.from(onboardingService.startOnboardingV2(onboardingRequest, user));
    }

    // 온보딩 루틴 등록 API (V2)
    @PostMapping("/v2/onboardings/routines")
    public CustomResponseDto<Object> registrationRoutinesV2(@RequestBody RegistrationRoutinesRequest registrationRoutinesRequest,
                                                            @CurrentUser User user) {
        onboardingService.registrationRoutinesV2(registrationRoutinesRequest, user);
        return CustomResponseDto.from(null);
    }

    // TODO: v2로 전환 시 deprecated 처리
    @Deprecated()
    @PostMapping("/v1/onboardings/routines")
    public CustomResponseDto<Object> registrationRoutines(@RequestBody RegistrationRoutinesRequest registrationRoutinesRequest,
                                                        @CurrentUser User user) {
        onboardingService.registrationRoutines(registrationRoutinesRequest, user);
        return CustomResponseDto.from(null);
    }
}
