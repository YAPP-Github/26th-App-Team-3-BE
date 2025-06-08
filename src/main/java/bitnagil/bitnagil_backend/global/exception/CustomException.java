package bitnagil.bitnagil_backend.global.exception;

import bitnagil.bitnagil_backend.global.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 커스텀 에외 클래스. 해당 클래스로 예외를 던져서 예외처리를 한다.
 */
@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

}