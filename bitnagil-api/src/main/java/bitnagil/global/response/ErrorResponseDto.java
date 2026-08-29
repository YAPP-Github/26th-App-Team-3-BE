package bitnagil.global.response;

import bitnagil.errorcode.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * API 요청 처리 중 발생한 에러 정보를 담는 응답 객체
 */
public class ErrorResponseDto extends ResponseDto{

    // 에러 코드와 메시지를 포함하는 기본 생성자
    private ErrorResponseDto(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
    }

    // 에러 코드와 예외 메시지를 포함하는 생성자
    private ErrorResponseDto(ErrorCode errorCode, Exception e) {
        super(errorCode.getCode(), errorCode.getMessage(e));
    }

    // 에러 코드와 예외 그리고 메세지를 포함하는 생성자
    private ErrorResponseDto(ErrorCode errorCode, Exception e, String message) {
        super(errorCode.getCode(), "errorMessage: " + errorCode.getMessage(e)
                + "\n" + "additional message: " + message);
    }

    public static ErrorResponseDto from(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode);
    }

    public static ErrorResponseDto of(ErrorCode errorCode, Exception e) {
        return new ErrorResponseDto(errorCode, e);
    }

    public static ErrorResponseDto of(ErrorCode errorCode, Exception e, String message) {
        return new ErrorResponseDto(errorCode, e, message);
    }
}