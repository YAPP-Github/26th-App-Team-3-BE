package bitnagil.user.dto.response;

import bitnagil.onboarding.domain.enums.EmotionType;
import bitnagil.onboarding.domain.enums.TargetOutingFrequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Builder
public class UserOnboardingResponse {
    private LocalTime timeSlot;
    private EmotionType emotionType;
    private TargetOutingFrequency targetOutingFrequency;

    public static UserOnboardingResponse from(
        bitnagil.user.dto.response.UserOnboardingResponse domainResponse) {
        return UserOnboardingResponse.builder()
            .timeSlot(domainResponse.getTimeSlot())
            .emotionType(domainResponse.getEmotionType())
            .targetOutingFrequency(domainResponse.getTargetOutingFrequency())
            .build();
    }
}
