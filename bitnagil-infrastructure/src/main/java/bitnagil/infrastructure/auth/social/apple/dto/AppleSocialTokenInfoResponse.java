package bitnagil.infrastructure.auth.social.apple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 애플 로그인 후 받은 토큰 정보를 매핑하는 응답 클래스입니다.
 */
@Getter
public class AppleSocialTokenInfoResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;
}
