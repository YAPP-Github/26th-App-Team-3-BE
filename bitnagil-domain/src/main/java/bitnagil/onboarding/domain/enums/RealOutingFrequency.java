package bitnagil.onboarding.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RealOutingFrequency implements EnumType {

    OFTEN("자주 외출해요"),
    SOMETIMES("가끔 나가요"),
    NEVER("밖에 나가지 않고 집에만 있어요"),
    SHORT("잠깐 외출했어요");
    ;

    private final String description;
}
