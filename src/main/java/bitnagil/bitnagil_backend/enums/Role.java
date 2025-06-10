package bitnagil.bitnagil_backend.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role implements EnumType {

    USER("ROLE_USER");

    private final String description;

    @Override
    public String getDescription() {
        return this.description;
    }
}
