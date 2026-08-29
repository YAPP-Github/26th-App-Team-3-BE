package bitnagil.user.domain.enums;

import bitnagil.enums.EnumType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role implements EnumType {

    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    WITHDRAWN("ROLE_WITHDRAWN"),
    ONBOARDING("ROLE_ONBOARDING"),;

    private final String description;

    @Override
    public String getDescription() {
        return this.description;
    }
}
