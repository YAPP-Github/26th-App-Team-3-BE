package bitnagil.infrastructure.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Token {
    private final String accessToken;

    private final String refreshToken;

    private final Long accessTokenExpiresIn;

    @Builder
    public Token(String accessToken, String refreshToken, Long accessTokenExpiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }
}
