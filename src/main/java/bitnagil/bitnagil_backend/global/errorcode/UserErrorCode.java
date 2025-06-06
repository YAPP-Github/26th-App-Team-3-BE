package bitnagil.bitnagil_backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{

    INACTIVE_USER(HttpStatus.FORBIDDEN, "사용할 수 없는 사용자입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
