package bitnagil.bitnagil_backend.infrastructure.oauth2.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import bitnagil.bitnagil_backend.infrastructure.oauth2.CustomOAuth2User;
import bitnagil.bitnagil_backend.infrastructure.oauth2.OAuth2Attribute;
import bitnagil.bitnagil_backend.user.Repository.UserRepository;
import bitnagil.bitnagil_backend.enums.SocialType;
import bitnagil.bitnagil_backend.user.entity.User;
import lombok.RequiredArgsConstructor;

/**
 * OAuth2 로그인 과정에서 사용자 정보를 처리하는 서비스 클래스입니다.
 *
 * Spring Security의 {@link OAuth2UserService}를 구현하여,
 * 외부 소셜 로그인(Kakao 등) 후 사용자 정보를 파싱하고 DB에 저장 또는 조회하여
 * 인증된 {@link CustomOAuth2User} 객체를 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String KAKAO = "kakao";
    private final UserRepository userRepository;

    /**
     * OAuth2 로그인 시 호출되는 메서드로, 외부 서비스에서 사용자 정보를 받아와 처리합니다.
     *
     * @param userRequest OAuth2 로그인 요청 정보
     * @return 인증된 사용자 정보를 담은 {@link CustomOAuth2User}
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);

        String userNameAttributeName = getUserNameAttributeName(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2Attribute extractAttributes = OAuth2Attribute.of(socialType, userNameAttributeName, attributes);

        // 기존 회원 조회 또는 신규 회원 저장
        User createdUser = getMember(extractAttributes, socialType);

        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getDescription())),
            attributes, extractAttributes.getNameAttributeKey(), createdUser.getUserId(), createdUser.getRole());
    }

    private String getUserNameAttributeName(final OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();
    }

    private SocialType getSocialType(String registrationId) {

        if (KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.APPLE;
    }

    private User getMember(OAuth2Attribute attributes, SocialType socialType) {
        User findUser = userRepository.findBySocialTypeAndSocialId(socialType, attributes.getSocialId()).orElse(null);

        if (findUser == null) {
            return saveMember(attributes, socialType);
        }
        return findUser;
    }

    private User saveMember(OAuth2Attribute attributes, SocialType socialType) {
        User createdUser = attributes.toEntity(socialType);
        return userRepository.save(createdUser);
    }
}