package bitnagil.user.dto.response;

import java.time.LocalTime;

import bitnagil.onboarding.domain.enums.EmotionType;
import bitnagil.onboarding.domain.enums.TargetOutingFrequency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserOnboardingResponse {

    @Schema(description = "잘 보내고 싶은 시간대",
        example = "08:00:00",
        required = true)
    private LocalTime timeSlot;

    @Schema(description = "요즘 필요한 회복 타입",
        example = "STABILITY",
        required = true)
    private EmotionType emotionType;

    @Schema(description = "일주일동안 목표 외출 횟수",
        example = "ONE_PER_WEEK",
        required = true)
    private TargetOutingFrequency targetOutingFrequency;
}