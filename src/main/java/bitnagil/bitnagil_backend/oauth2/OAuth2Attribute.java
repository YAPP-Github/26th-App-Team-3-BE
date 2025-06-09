package bitnagil.bitnagil_backend.oauth2;

import java.util.HashMap;
import java.util.Map;

import bitnagil.bitnagil_backend.enums.SocialType;
import bitnagil.bitnagil_backend.user.entity.User;
import bitnagil.bitnagil_backend.enums.Role;
import lombok.Builder;
import lombok.Getter;

/**
 * OAuth2 로그인 과정에서 소셜 플랫폼(Kakao 등)으로부터 전달받은 사용자 정보를 담고,
 * 필요한 형태로 변환하여 도메인 객체(User)로 매핑하는 역할을 수행하는 클래스입니다.
 */
@Getter
public class OAuth2Attribute {

    private final Map<String, Object> attributes; // 전체 사용자 정보
    private final String nameAttributeKey;        // OAuth2 로그인에서 사용자 식별에 사용하는 키
    private final String socialId;                // 카카오 id
    private final String email;                   // 사용자 이메일

    @Builder
    public OAuth2Attribute(Map<String, Object> attributes, String nameAttributeKey,
        String socialId, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.socialId = socialId;
        this.email = email;
    }

    public static OAuth2Attribute of(SocialType socialType, String userNameAttributeName,
        Map<String, Object> attributes) {
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        throw new IllegalArgumentException("Unsupported social type: " + socialType);
    }

    private static OAuth2Attribute ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String socialId = String.valueOf(attributes.get("id"));
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;

        // 전체 속성: socialId + kakao_account를 합친 Map
        Map<String, Object> mergedAttributes = new HashMap<>();
        mergedAttributes.put("socialId", socialId);
        if (kakaoAccount != null) mergedAttributes.putAll(kakaoAccount);

        return OAuth2Attribute.builder()
            .attributes(mergedAttributes)
            .nameAttributeKey(userNameAttributeName)
            .socialId(socialId)
            .email(email)
            .build();
    }

    public User toEntity(SocialType socialType) {
        return User.builder()
            .socialType(socialType)
            .socialId(socialId)
            .role(Role.USER)
            .build();
    }
}
