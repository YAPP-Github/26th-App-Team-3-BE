package bitnagil.bitnagil_backend.auth.jwt.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 *  로그인 후 토큰 관련 JSON 정보를 담은 클래스입니다.
 */
@Getter
@AllArgsConstructor
@Builder
public class LoginResponse {
    @NotEmpty
    private String accessToken;

    @NotEmpty
    private String refreshToken;

    private Long accessTokenExpiresIn;

    public static LoginResponse of(Token token) {
        return LoginResponse.builder()
            .accessToken(token.getAccessToken())
            .refreshToken(token.getRefreshToken())
            .accessTokenExpiresIn(token.getAccessTokenExpiresIn())
            .build();
    }
}
