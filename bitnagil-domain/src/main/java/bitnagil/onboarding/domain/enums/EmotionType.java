package bitnagil.onboarding.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmotionType implements EnumType {
    STABILITY("안정감"), // 안정감
    CONNECTEDNESS("연결감"), // 연결감
    VITALITY("생동감"), // 생동감,
    GROWTH("성장감"), // 성장감
    ;

    private final String description;
}
