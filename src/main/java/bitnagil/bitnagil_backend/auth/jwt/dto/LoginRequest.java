package bitnagil.bitnagil_backend.auth.jwt.dto;

import bitnagil.bitnagil_backend.enums.SocialType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * 카카오 로그인 시에 필요한 요청의 JSON 정보를 담는 클래스입니다.
 */
@Getter
@Builder
public class LoginRequest {
    @NotNull
    private SocialType socialType;

    @NotEmpty
    private String code;

    @NotEmpty
    private String redirectUri;
}
