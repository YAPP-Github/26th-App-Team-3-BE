package bitnagil.onboarding.dto.request;


import bitnagil.onboarding.domain.enums.RealOutingFrequency;
import bitnagil.onboarding.domain.enums.TargetOutingFrequency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "온보딩 요청 DTO")
@AllArgsConstructor
public class OnboardingRequestV2 {

    @Schema(description = "어떤 시간대를 더 잘 보내고 싶나요?", required = true, example = "08:00:00")
    private LocalTime timeSlot;
    @Schema(description = "요즘 어떤 회복이 필요하신가요?", required = true)
    private List<String> emotionType;
    @Schema(description = "최근 얼마나 자주 바깥바람을 쐬시나요?", required = true)
    private RealOutingFrequency realOutingFrequency;
    @Schema(description = "일주일에 몇번 외출하고 싶으신가요?",  required = true)
    private TargetOutingFrequency targetOutingFrequency;

}
