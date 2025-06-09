package bitnagil.bitnagil_backend.global.errorcode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JwtErrorCode는 토큰 관련 에러 코드들을 정의하는 열거형 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    FORBIDDEN_USER("JW000", HttpStatus.FORBIDDEN, "필요한 권한이 없는 사용자입니다."),
    UNAUTHENTICATED_USER("JW001", HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
