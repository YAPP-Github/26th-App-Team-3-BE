package bitnagil.bitnagil_backend.infrastructure.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 카카오 OAuth2 인증 서버로부터 토큰 응답을 받을 때 사용되는 DTO 클래스입니다.
 *
 * 카카오 로그인 요청 후 받은 access token, refresh token 등의 값을
 * Jackson의 @JsonProperty 어노테이션을 통해 매핑합니다.
 */
@Getter
@Setter
public class KakaoTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;
}
