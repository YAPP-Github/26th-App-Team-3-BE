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
    INVALID_SIGNATURE("JW002", HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    EXPIRED_TOKEN("JW003", HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN("JW004", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    MALFORMED_TOKEN("JW005", HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
