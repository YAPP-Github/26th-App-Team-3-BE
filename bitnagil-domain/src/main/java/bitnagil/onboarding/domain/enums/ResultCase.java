package bitnagil.onboarding.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResultCase implements EnumType {
    CASE1("케이스1"),
    CASE2("케이스2"),
    CASE3("케이스3"),
    CASE4("케이스4"),
    ;

    private final String description;
}
