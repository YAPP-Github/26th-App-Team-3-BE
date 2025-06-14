package bitnagil.bitnagil_backend.auth.oauth2.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bitnagil.bitnagil_backend.auth.oauth2.service.AuthService;
import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
import bitnagil.bitnagil_backend.auth.jwt.request.LoginRequest;
import bitnagil.bitnagil_backend.auth.jwt.response.TokenResponse;
import bitnagil.bitnagil_backend.enums.SocialType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public CustomResponseDto<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        if (loginRequest.getSocialType().equals(SocialType.KAKAO)) {

            TokenResponse tokenResponse = authService.kakaoLogin(loginRequest.getCode(), loginRequest.getRedirectUri());

            return CustomResponseDto.from(tokenResponse);
        }

        // TODO 애플 로그인 추가
        return CustomResponseDto.from(null);
    }

    @PostMapping("/token/reissue")
    public CustomResponseDto<TokenResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        TokenResponse tokenResponse = authService.reissueToken(refreshToken);

        return CustomResponseDto.from(tokenResponse);

        // TODO 애플 로그인 관련 토큰 재발행 추가
    }

}
