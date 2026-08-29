package bitnagil.userOnboardingInfo.service;


import bitnagil.user.domain.User;
import bitnagil.userOnboardingInfo.domain.UserOnboardingInfo;
import bitnagil.userOnboardingInfo.repository.UserOnboardingInfoRepository;
import bitnagil.userOnboardingInfo.response.UserOnboardingInfoSearchResponse;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserOnboardingInfoService {

    private final UserOnboardingInfoRepository userOnboardingInfoRepository;

    @Transactional
    public UserOnboardingInfoSearchResponse getUserOnboardingInfo (User user) {
        UserOnboardingInfo userOnboardingInfo = userOnboardingInfoRepository.findByUser(user);

        if (userOnboardingInfo == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER_ONBOARDING_INFO);
        }

        return UserOnboardingInfoSearchResponse.builder()
                .timeSlot(userOnboardingInfo.getTimeSlot())
                .emotionTypes(userOnboardingInfo.getEmotionTypes())
                .targetOutingFrequency(userOnboardingInfo.getTargetOutingFrequency())
                .build();
    }
}
