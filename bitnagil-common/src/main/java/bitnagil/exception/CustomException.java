package bitnagil.exception;

import bitnagil.errorcode.ErrorCode;
import lombok.Getter;

/**
 * 커스텀 에외 클래스. 해당 클래스로 예외를 던져서 예외처리를 한다.
 */
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // ← 메시지 설정
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}