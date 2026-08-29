package bitnagil.changedRoutine.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChangedDivCode implements EnumType {
    TODAY_CHANGE("당일 변경(ex: 루틴명, 세부루틴명, 실행시간 등)"),
    DELAY("미루기"),
    TODAY_DELETE("오늘만 루틴 삭제"),
    ONBOARDING("온보딩 루틴")
    ;

    private final String description;
}
