package bitnagil.bitnagil_backend.auth.oauth2.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bitnagil.bitnagil_backend.auth.oauth2.service.AuthService;
import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
import bitnagil.bitnagil_backend.auth.jwt.LoginRequest;
import bitnagil.bitnagil_backend.auth.jwt.TokenResponse;
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
        TokenResponse tokenResponse = authService.socialLogin(loginRequest);

        return CustomResponseDto.from(tokenResponse);
    }

    @PostMapping("/token/reissue")
    public CustomResponseDto<TokenResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        TokenResponse tokenResponse = authService.reissueToken(refreshToken);

        return CustomResponseDto.from(tokenResponse);
    }

}
