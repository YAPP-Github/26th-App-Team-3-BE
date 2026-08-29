package bitnagil.auth.service;

import bitnagil.auth.dto.request.UserLoginRequest;
import bitnagil.auth.dto.response.UserTokenResponse;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.infrastructure.auth.jwt.AuthRedisService;
import bitnagil.infrastructure.auth.jwt.JwtTokenService;
import bitnagil.infrastructure.auth.jwt.RefreshToken;
import bitnagil.infrastructure.auth.jwt.Token;
import bitnagil.infrastructure.auth.social.apple.AppleUserInfoService;
import bitnagil.infrastructure.auth.social.apple.dto.AppleIdTokenPayload;
import bitnagil.infrastructure.auth.social.kakao.KakaoUserInfoService;
import bitnagil.infrastructure.auth.social.kakao.dto.KakaoUserInfoResponse;
import bitnagil.user.domain.User;
import bitnagil.user.domain.enums.Role;
import bitnagil.user.domain.enums.SocialType;
import bitnagil.user.dto.request.UserAgreementsRequest;
import bitnagil.user.dto.request.UserWithdrawalRequest;
import bitnagil.user.repository.UserRepository;
import bitnagil.user.service.UserManager;
import bitnagil.user.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final UserManager userManager;
    private final UserService userService;
    private final KakaoUserInfoService kakaoUserInfoService;
    private final AppleUserInfoService appleUserInfoService;
    private final JwtTokenService jwtTokenService;
    private final AuthRedisService authRedisService;

    @Transactional
    public UserTokenResponse login(UserLoginRequest request, String socialAccessToken) {
        SocialLoginUserInfo socialUserInfo = getSocialUserInfo(request, socialAccessToken);
        User user = signUpOrLogin(request, socialUserInfo);
        Token token = jwtTokenService.generateToken(user.getUserId());

        return UserTokenResponse.builder()
            .accessToken(token.getAccessToken())
            .refreshToken(token.getRefreshToken())
            .role(user.getRole())
            .build();
    }

    @Transactional
    public UserTokenResponse reissueToken(String refreshToken) {
        if (!jwtTokenService.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
        }

        Long userId = jwtTokenService.extractUserId(refreshToken);
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        RefreshToken refreshTokenByRedis = authRedisService.getRefreshTokenByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_JWT_TOKEN));

        if (!refreshTokenByRedis.getRefreshToken().equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
        }

        Token token = jwtTokenService.generateToken(user.getUserId());

        return UserTokenResponse.builder()
            .accessToken(token.getAccessToken())
            .refreshToken(token.getRefreshToken())
            .role(user.getRole())
            .build();
    }

    @Transactional
    public void logout(User user) {
        authRedisService.deleteRefreshToken(user.getUserId());

        if (user.getSocialType() == SocialType.KAKAO) {
            kakaoUserInfoService.logout(user.getSocialId());
        }
    }

    @Transactional
    public void withdrawal(UserWithdrawalRequest request, User user) {
        User persistedUser = userManager.getPersistedUser(user);
        authRedisService.deleteRefreshToken(persistedUser.getUserId());

        userService.withdraw(persistedUser, request.getReasonOfWithdrawal());
        unlinkFromSocial(persistedUser);
    }

    @Transactional
    public void agreements(UserAgreementsRequest request, User user) {
        userService.agreements(request, user);
    }

    private SocialLoginUserInfo getSocialUserInfo(UserLoginRequest request, String socialAccessToken) {
        if (request.getSocialType() == SocialType.KAKAO) {
            KakaoUserInfoResponse kakaoUserInfoResponse = kakaoUserInfoService.get(socialAccessToken);
            return new SocialLoginUserInfo(
                kakaoUserInfoResponse.getId(),
                kakaoUserInfoResponse.getKakaoAccount().getProfile().getNickname(),
                kakaoUserInfoResponse.getKakaoAccount().getEmail(),
                null
            );
        }

        AppleIdTokenPayload appleIdTokenPayload = appleUserInfoService.get(socialAccessToken);
        if (appleIdTokenPayload.getEmail() == null) {
            throw new CustomException(ErrorCode.APPLE_UNLINK_PENDING);
        }

        return new SocialLoginUserInfo(
            appleIdTokenPayload.getSub(),
            request.getNickname(),
            appleIdTokenPayload.getEmail(),
            appleIdTokenPayload.getRefreshToken()
        );
    }

    private User signUpOrLogin(UserLoginRequest request, SocialLoginUserInfo socialUserInfo) {
        Optional<User> existingUser = userRepository.findBySocialTypeAndSocialId(
            request.getSocialType(),
            socialUserInfo.socialId()
        );

        return existingUser.orElseGet(() -> saveUser(request.getSocialType(), socialUserInfo));
    }

    private User saveUser(SocialType socialType, SocialLoginUserInfo socialUserInfo) {
        User user = User.builder()
            .socialType(socialType)
            .socialId(socialUserInfo.socialId())
            .role(Role.GUEST)
            .email(socialUserInfo.email())
            .nickname(socialUserInfo.nickname())
            .refreshToken(socialUserInfo.refreshToken())
            .build();

        return userRepository.save(user);
    }

    private void unlinkFromSocial(User user) {
        if (user.getSocialType() == SocialType.KAKAO) {
            kakaoUserInfoService.unlink(user.getSocialId());
            return;
        }

        if (user.getSocialType() == SocialType.APPLE) {
            appleUserInfoService.unlink(user.getRefreshToken());
        }
    }

    private record SocialLoginUserInfo(
        String socialId,
        String nickname,
        String email,
        String refreshToken
    ) {
    }
}
