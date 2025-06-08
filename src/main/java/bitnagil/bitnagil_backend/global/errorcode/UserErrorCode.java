package bitnagil.bitnagil_backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * UserErrorCode는 사용자 관련 에러 코드들을 정의하는 열거형 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{

    INACTIVE_USER("US000", HttpStatus.FORBIDDEN, "사용할 수 없는 사용자입니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
