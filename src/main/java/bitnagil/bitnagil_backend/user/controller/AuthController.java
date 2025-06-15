package bitnagil.bitnagil_backend.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bitnagil.bitnagil_backend.auth.jwt.TokenResponse;
import bitnagil.bitnagil_backend.enums.SocialType;
import bitnagil.bitnagil_backend.user.service.AuthService;
import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public CustomResponseDto<TokenResponse> login(
        @RequestParam("socialType") SocialType socialType,
            @RequestHeader("Authorization") String socialAccessToken) {

        TokenResponse tokenResponse = authService.socialLogin(socialType, socialAccessToken);

        return CustomResponseDto.from(tokenResponse);
    }

    @PostMapping("/token/reissue")
    public CustomResponseDto<TokenResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {

        TokenResponse tokenResponse = authService.reissueToken(refreshToken);

        return CustomResponseDto.from(tokenResponse);
    }

}
