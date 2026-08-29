package bitnagil.onboarding.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TimeSlot implements EnumType {
    MORNING("아침을 잘 시작하고 싶어요"), // 아침을 잘 시작하고 싶어요
    EVENING("저녁을 편안하게 마무리하고 싶어요"), // 저녁을 편안하게 마무리하고 싶어요
    NOTHING("딱히 상관 없어요"), // 딱히 상관 없어요
    ;

    private final String description;
}
