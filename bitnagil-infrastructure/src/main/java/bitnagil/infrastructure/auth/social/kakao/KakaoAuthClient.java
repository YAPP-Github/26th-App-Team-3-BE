package bitnagil.infrastructure.auth.social.kakao;

import bitnagil.infrastructure.auth.social.kakao.dto.KakaoUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 카카오 서버와 통신하는 클래스입니다.
 */
@FeignClient(
    name = "KakaoAuthFeignClient",
    url = "${spring.security.oauth2.client.provider.kakao-provider.base-uri}",
    configuration = KakaoFeignClientConfiguration.class
)
public interface KakaoAuthClient {

    @GetMapping("/v2/user/me")
    KakaoUserInfoResponse getUserInfo(@RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/v1/user/logout")
    String logout(
        @RequestHeader("Authorization") String adminKey,
        @RequestHeader("Content-Type") String contentType,
        @RequestParam("target_id_type") String targetIdType,
        @RequestParam("target_id") Long socialId
    );

    @PostMapping("/v1/user/unlink")
    String unlink(
        @RequestHeader("Authorization") String adminKey,
        @RequestHeader("Content-Type") String contentType,
        @RequestParam("target_id_type") String targetIdType,
        @RequestParam("target_id") Long socialId
    );
}
