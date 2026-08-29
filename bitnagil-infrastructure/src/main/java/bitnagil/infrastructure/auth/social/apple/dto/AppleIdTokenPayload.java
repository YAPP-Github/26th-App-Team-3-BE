package bitnagil.infrastructure.auth.social.apple.dto;

import lombok.Getter;

/**
 * 애플 ID 토큰 페이로드 클래스입니다.
 */
@Getter
public class AppleIdTokenPayload {

    private String sub;
    private String email;
    private String name;
    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
