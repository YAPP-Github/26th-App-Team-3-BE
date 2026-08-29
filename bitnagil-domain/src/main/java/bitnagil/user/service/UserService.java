package bitnagil.user.service;

import bitnagil.onboarding.domain.Onboarding;
import bitnagil.user.dto.request.UserAgreementsRequest;
import bitnagil.user.dto.response.UserInfoResponse;
import bitnagil.user.dto.response.UserOnboardingResponse;
import bitnagil.user.domain.User;
import bitnagil.user.repository.UserRepository;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 유저 정보를 관리하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserManager userManager;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(User user) {
        return userMapper.toUserInfoResponse(user);
    }

    // 회원탈퇴 - 회원 관련 정보 삭제
    // 토큰 무효화 및 unlinking은 API 모듈에서 구현
    @Transactional
    public void withdraw(User user, String reason) {
        user.updateUserReasonOfWithdrawal(reason);
        userRepository.flush();
        userRepository.delete(user);
    }

    // 약관 동의 - 회원의 ROLE을 USER로 업데이트
    @Transactional
    public void agreements(UserAgreementsRequest userAgreeMentsRequest, User user) {
        // 약관 동의 시 ROLE을 USER로 변경 및 동의 여부 업데이트
        User persistedUser = userManager.getPersistedUser(user);

        if(userAgreeMentsRequest.getAgreedToTermsOfService() == false ||
                userAgreeMentsRequest.getAgreedToPrivacyPolicy() == false ||
                userAgreeMentsRequest.getIsOverFourteen() == false) {
            throw new CustomException(ErrorCode.AGREEMENT_NOT_ACCEPTED);
        }

        persistedUser.updateAgreements(userAgreeMentsRequest.getAgreedToTermsOfService(),
                userAgreeMentsRequest.getAgreedToPrivacyPolicy(),
                userAgreeMentsRequest.getIsOverFourteen());
    }

    // 회원 온보딩 정보 조회
    @Transactional(readOnly = true)
    public UserOnboardingResponse getUserOnboarding(User user) {
        User persistedUser = userManager.getPersistedUser(user);
        Onboarding onboarding = persistedUser.getOnboarding();

        return userMapper.toUserOnboardingResponse(
            onboarding.getTimeSlot(),
            onboarding.getEmotionType(),
            onboarding.getTargetOutingFrequency());
    }
}
