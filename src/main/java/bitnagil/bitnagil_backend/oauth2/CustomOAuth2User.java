package bitnagil.bitnagil_backend.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import bitnagil.bitnagil_backend.enums.Role;
import lombok.Getter;

/**
 * OAuth2 로그인 이후 인증된 사용자 정보를 담는 커스텀 OAuth2 사용자 클래스입니다.
 *
 * Spring Security의 {@link DefaultOAuth2User}를 확장하여,
 * 기본 OAuth2 사용자 정보 외에도 애플리케이션 도메인에서 사용하는 `userId`와 `userRole`을 추가로 포함합니다.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final Long userId;
    private final Role userRole;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes, String nameAttributeKey, Long userId, Role userRole) {
        super(authorities, attributes, nameAttributeKey);
        this.userId = userId;
        this.userRole = userRole;
    }
}