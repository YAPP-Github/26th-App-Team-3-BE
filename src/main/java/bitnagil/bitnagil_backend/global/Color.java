package bitnagil.bitnagil_backend.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {
    RED("#ff0000")
    ;

    private final String code;
}