package bitnagil.bitnagil_backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * ErrorCode는 모든 에러 코드를 정의한 Enum입니다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 에러 코드
    OK("CO000", HttpStatus.OK, "OK"),
    INVALID_PARAMETER("CO001", HttpStatus.BAD_REQUEST, "올바르지 않은 파라미터입니다."),
    RESOURCE_NOT_FOUND("CO002", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("CO003", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    NOT_FOUND_HANDLER("CO004", HttpStatus.NOT_FOUND, "요청한 핸들러를 찾을 수 없습니다."),

    // JWT 관련 에러 코드
    FORBIDDEN_USER("JW000", HttpStatus.FORBIDDEN, "필요한 권한이 없는 사용자입니다."),
    UNAUTHENTICATED_USER("JW001", HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    // User 관련 에러 코드
    INACTIVE_USER("US000", HttpStatus.FORBIDDEN, "사용할 수 없는 사용자입니다."),

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable throwable) {
        return this.getMessage(this.getMessage(this.getMessage() + " - " + throwable.getMessage()));
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }


}