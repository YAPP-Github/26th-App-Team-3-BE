package bitnagil.userOnboardingInfo.response;

import bitnagil.onboarding.domain.enums.TargetOutingFrequency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserOnboardingInfoSearchResponse {

    @Schema(example = "08:00:00")
    private LocalTime timeSlot;

    @Schema(example = "[\"GROWTH\", \"VITALITY\"]")
    private List<String> emotionTypes;

    @Schema(example = "ONE_PER_WEEK")
    private TargetOutingFrequency targetOutingFrequency;

}
