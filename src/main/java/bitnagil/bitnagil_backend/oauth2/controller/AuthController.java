package bitnagil.bitnagil_backend.oauth2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bitnagil.bitnagil_backend.jwt.dto.LoginRequest;
import bitnagil.bitnagil_backend.jwt.dto.LoginResponse;
import bitnagil.bitnagil_backend.enums.SocialType;
import bitnagil.bitnagil_backend.oauth2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        if (loginRequest.getSocialType().equals(SocialType.KAKAO)) {
            return ResponseEntity.status(HttpStatus.OK.value())
                .body(authService.kakaoLogin(loginRequest.getCode(), loginRequest.getRedirectUri()));
        }

        // TODO 애플 로그인 추가
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(null);
    }
}
