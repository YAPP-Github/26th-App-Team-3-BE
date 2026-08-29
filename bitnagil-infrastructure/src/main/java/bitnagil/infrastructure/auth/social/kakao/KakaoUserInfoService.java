package bitnagil.infrastructure.auth.social.kakao;

import bitnagil.infrastructure.auth.social.kakao.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 카카오 서버 API 호출을 관리하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class KakaoUserInfoService {
    private static final String KAKAO_AUTH_PREFIX = "KakaoAK ";
    private static final String AUTHORIZATION_TYPE = "Bearer ";
    private static final String FORM_URLENCODED = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String TARGET_ID_TYPE = "user_id";

    @Value("${spring.security.oauth2.client.registration.kakao.admin-key}")
    private String kakaoAdminKey;

    private final KakaoAuthClient kakaoAuthClient;

    public KakaoUserInfoResponse get(String accessToken) {
        return kakaoAuthClient.getUserInfo(AUTHORIZATION_TYPE + accessToken);
    }

    public void unlink(String socialId) {
        kakaoAuthClient.unlink(
            KAKAO_AUTH_PREFIX + kakaoAdminKey,
            FORM_URLENCODED,
            TARGET_ID_TYPE,
            Long.valueOf(socialId)
        );
    }

    public void logout(String socialId) {
        kakaoAuthClient.logout(
            KAKAO_AUTH_PREFIX + kakaoAdminKey,
            FORM_URLENCODED,
            TARGET_ID_TYPE,
            Long.valueOf(socialId)
        );
    }
}
