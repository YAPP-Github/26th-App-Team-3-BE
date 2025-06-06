package bitnagil.bitnagil_backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * CommonErrorCode는 공통으로 사용되는 에러 코드들을 정의하는 열거형 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{

    OK("CO000", HttpStatus.OK, "OK"),
    INVALID_PARAMETER("CO001", HttpStatus.BAD_REQUEST, "올바르지 않은 파라미터입니다."),
    RESOURCE_NOT_FOUND("CO002", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("CO003", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    NOT_FOUND_HANDLER("CO004", HttpStatus.NOT_FOUND, "요청한 핸들러를 찾을 수 없습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
