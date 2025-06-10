package bitnagil.bitnagil_backend.infrastructure.oauth2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bitnagil.bitnagil_backend.infrastructure.jwt.dto.Token;
import bitnagil.bitnagil_backend.infrastructure.jwt.service.JwtTokenProvider;
import bitnagil.bitnagil_backend.infrastructure.jwt.service.UserAuthentication;
import bitnagil.bitnagil_backend.infrastructure.oauth2.dto.KakaoTokenResponse;
import bitnagil.bitnagil_backend.user.Repository.UserRepository;
import bitnagil.bitnagil_backend.enums.SocialType;
import bitnagil.bitnagil_backend.infrastructure.jwt.dto.LoginResponse;
import bitnagil.bitnagil_backend.user.entity.User;
import bitnagil.bitnagil_backend.enums.Role;
import bitnagil.bitnagil_backend.infrastructure.oauth2.dto.KakaoAccount;
import bitnagil.bitnagil_backend.infrastructure.oauth2.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;

/**
 * 소셜 로그인 인증을 처리하는 서비스 클래스입니다.
 *
 * 추후 Apple 로그인이 추가될 예정
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final OAuth2TokenService oauth2TokenService;

    @Transactional
    public LoginResponse kakaoLogin(String code, String kakaoRedirectUrl) {

        KakaoTokenResponse tokenResponse = oauth2TokenService.getKakaoToken(kakaoClientId,
            kakaoRedirectUrl, code);

        KakaoUserInfo userInfo = oauth2TokenService.getUserInfo(tokenResponse.getAccessToken());

        User member = signUpOrLogin(SocialType.KAKAO, userInfo.getId(), userInfo.getKakaoAccount());

        Token token = jwtTokenProvider.generateToken(new UserAuthentication(member.getUserId(), null, null));

        return LoginResponse.of(token);
    }

    private User signUpOrLogin(SocialType socialType, String socialId, KakaoAccount kakaoAccount) {
        return userRepository.findBySocialTypeAndSocialId(socialType, socialId)
            .orElseGet(() -> saveMember(socialType, socialId, kakaoAccount));
    }

    private User saveMember(SocialType socialType, String socialId, KakaoAccount kakaoAccount) {
        User user = User.builder()
            .socialType(socialType)
            .socialId(socialId)
            .role(Role.USER)
            .email(kakaoAccount.getEmail())
            .build();

        return userRepository.save(user);
    }
}