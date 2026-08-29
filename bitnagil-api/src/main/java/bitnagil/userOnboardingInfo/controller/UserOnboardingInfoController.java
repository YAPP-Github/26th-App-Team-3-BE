package bitnagil.userOnboardingInfo.controller;

import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.userOnboardingInfo.dto.response.UserOnboardingInfoSearchResponse;
import bitnagil.user.domain.User;
import bitnagil.userOnboardingInfo.controller.spec.UserOnboardingInfoSpec;
import bitnagil.userOnboardingInfo.service.UserOnboardingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UserOnboardingInfoController implements UserOnboardingInfoSpec {

    private final UserOnboardingInfoService userOnboardingInfoService;

    @GetMapping("/v2/onboardings")
    public CustomResponseDto<UserOnboardingInfoSearchResponse> getUserOnboardingInfo(@CurrentUser User user) {
        return CustomResponseDto.from(
            UserOnboardingInfoSearchResponse.from(userOnboardingInfoService.getUserOnboardingInfo(user)));
    }
}
