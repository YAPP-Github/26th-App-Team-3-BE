package bitnagil.bitnagil_backend.infrastructure.oauth2.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bitnagil.bitnagil_backend.global.errorcode.CommonErrorCode;
import bitnagil.bitnagil_backend.infrastructure.oauth2.service.AuthService;
import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
import bitnagil.bitnagil_backend.infrastructure.jwt.dto.LoginRequest;
import bitnagil.bitnagil_backend.infrastructure.jwt.dto.LoginResponse;
import bitnagil.bitnagil_backend.enums.SocialType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public CustomResponseDto<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        if (loginRequest.getSocialType().equals(SocialType.KAKAO)) {

            LoginResponse loginResponse = authService.kakaoLogin(loginRequest.getCode(), loginRequest.getRedirectUri());

            return CustomResponseDto.from(loginResponse);
        }

        // TODO 애플 로그인 추가
        return CustomResponseDto.from(null);
    }
}
