package bitnagil.infrastructure.auth.social.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 동의 항목 정보를 받는 클래스입니다.
 */
@Getter
@NoArgsConstructor
public class KakaoAccount {
    private Profile profile;
    private String email;

    @Getter
    @NoArgsConstructor
    public static class Profile {
        private String nickname;
    }
}
