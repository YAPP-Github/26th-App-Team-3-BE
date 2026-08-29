package bitnagil.auth.controller;

import bitnagil.auth.controller.spec.UserAuthSpec;
import bitnagil.auth.dto.request.UserLoginRequest;
import bitnagil.auth.dto.response.UserTokenResponse;
import bitnagil.auth.service.UserAuthService;
import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.user.domain.User;
import bitnagil.user.dto.request.UserAgreementsRequest;
import bitnagil.user.dto.request.UserWithdrawalRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthController implements UserAuthSpec {
    private final UserAuthService userAuthService;

    @PostMapping("/login")
    public CustomResponseDto<UserTokenResponse> login(
        @Valid @RequestBody UserLoginRequest userLoginRequest,
        @RequestHeader("SocialAccessToken") String socialAccessToken
    ) {
        return CustomResponseDto.from(userAuthService.login(userLoginRequest, socialAccessToken));
    }

    @PostMapping("/logout")
    public CustomResponseDto<Object> logout(@CurrentUser User user) {
        userAuthService.logout(user);
        return CustomResponseDto.from(null);
    }

    @PostMapping("/token/reissue")
    public CustomResponseDto<UserTokenResponse> reissueToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return CustomResponseDto.from(userAuthService.reissueToken(refreshToken));
    }

    @PostMapping("/withdrawal")
    public CustomResponseDto<Object> withdrawal(
        @RequestBody UserWithdrawalRequest userWithdrawalRequest,
        @CurrentUser User user
    ) {
        userAuthService.withdrawal(userWithdrawalRequest, user);
        return CustomResponseDto.from(null);
    }

    @PostMapping("/agreements")
    public CustomResponseDto<Void> agreements(
        @RequestBody UserAgreementsRequest userAgreementsRequest,
        @CurrentUser User user
    ) {
        userAuthService.agreements(userAgreementsRequest, user);
        return CustomResponseDto.from(null);
    }
}
