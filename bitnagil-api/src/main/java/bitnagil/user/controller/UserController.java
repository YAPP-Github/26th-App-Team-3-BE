package bitnagil.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.user.controller.spec.UserSpec;
import bitnagil.user.dto.response.UserInfoResponse;
import bitnagil.user.dto.response.UserOnboardingResponse;
import bitnagil.user.domain.User;
import bitnagil.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserController implements UserSpec {

    private final UserService userService;

    @GetMapping("/infos")
    public CustomResponseDto<UserInfoResponse> getUserInfo(@CurrentUser User user) {
        UserInfoResponse userInfoResponse = UserInfoResponse.from(userService.getUserInfo(user));

        return CustomResponseDto.from(userInfoResponse);
    }

    @GetMapping("/onboarding")
    public CustomResponseDto<UserOnboardingResponse> getUserOnboarding(@CurrentUser User user) {
        return CustomResponseDto.from(UserOnboardingResponse.from(userService.getUserOnboarding(user)));
    }
}
