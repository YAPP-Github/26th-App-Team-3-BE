package bitnagil.onboarding.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TargetOutingFrequency implements EnumType {

    ONE_PER_WEEK("일주일 1회"),
    TWO_TO_THREE_PER_WEEK("일주일 2~3회"),
    MORE_THAN_FOUR_PER_WEEK("일주일 4회 이상"),
    UNKNOWN("아직 잘 모르겠어요"),
    ;

    private final String description;
}
