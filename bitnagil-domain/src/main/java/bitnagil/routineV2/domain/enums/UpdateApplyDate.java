package bitnagil.routineV2.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UpdateApplyDate implements EnumType {

    TODAY("오늘부터 적용"),
    TOMORROW("내일부터 적용");

    private final String description;
}
