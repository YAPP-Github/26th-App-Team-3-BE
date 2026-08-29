package bitnagil.infrastructure.auth.social.apple;

import bitnagil.infrastructure.auth.social.apple.dto.AppleSocialTokenInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 애플 인증 관련 Feign 클라이언트입니다.
 */
@Component
@FeignClient(
    name = "apple-auth",
    url = "${client.apple-auth.url}",
    configuration = AppleFeignClientConfiguration.class
)
public interface AppleAuthClient {

    @PostMapping("/auth/token")
    AppleSocialTokenInfoResponse getIdToken(
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("grant_type") String grantType,
        @RequestParam("code") String code
    );

    @PostMapping("/auth/revoke")
    String revoke(
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("token") String refreshToken,
        @RequestParam("token_type_hint") String tokenTypeHint
    );
}
