package bitnagil.bitnagil_backend.auth.jwt.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

/**
 *  서비스 로직 중 Token 관련 정보를 담고 있는 클래스입니다.
 *  LoginResponse 와 필드는 동일하지만 쓰이는 용도의 차이를 두기 위함입니다.
 */
@Getter
public class Token {

    @NotEmpty
    private final String accessToken;

    @NotEmpty
    private final String refreshToken;

    private final Long accessTokenExpiresIn;

    @Builder
    public Token(String accessToken, String refreshToken, Long accessTokenExpiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }
}