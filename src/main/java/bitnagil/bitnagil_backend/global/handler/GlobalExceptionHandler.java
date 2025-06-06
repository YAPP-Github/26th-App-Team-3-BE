package bitnagil.bitnagil_backend.global.handler;

import bitnagil.bitnagil_backend.global.errorcode.CommonErrorCode;
import bitnagil.bitnagil_backend.global.errorcode.ErrorCode;
import bitnagil.bitnagil_backend.global.exception.CustomException;
import bitnagil.bitnagil_backend.global.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 커스텀 예외를 처리하는 메서드
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(final CustomException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    /**
     * 적절하지 않은 인자를 받았을 때 발생하는 예외를 처리하는 메서드
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    /**
     * @Valid가 붙은 @RequestBody DTO 객체의 유효성 검증 실패 시 발생하는 예외를 처리하는 메서드
     * 즉, JSON 형태로 넘어온 요청 본문이 DTO 제약 조건(@NotBlank, @Size 등)을 위반했을 때 발생
     */
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.warn("handleIllegalArgument", e);
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    /**
     * @Validated가 붙은 @RequestParam, @PathVariable, @ModelAttribute의 유효성 검증 실패 시 발생하는 예외를 처리하는 메서드
     * 즉, 쿼리 파라미터나 경로 변수, 폼 데이터의 제약 조건(@NotBlank, @Size 등)을 위반했을 때 발생
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException e) {
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    /**
     * @ModelAttribute를 사용하는 요청에서 바인딩 실패(타입 불일치, 필수 파라미터 누락 등) 시 발생하는 예외를 처리하는 메서드
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(final BindException e) {
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, errorCode);
    }

    /**
     * Exception.class 예외를 처리하는 메서드
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(final Exception ex) {
        log.warn("handleAllException", ex);
        final ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    /**
     * 에러 코드를 받아서 ResponseEntity를 생성하는 메서드
     */
    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    /**
     * 에러 코드와 메시지를 받아서 ResponseEntity를 생성하는 메서드
     */
    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode, final String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    /**
     * 에러 코드를 받아서 ErrorResponse를 생성하는 메서드
     */
    private ErrorResponse makeErrorResponse(final ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    /**
     * 에러 코드와 메시지를 받아서 ErrorResponse를 생성하는 메서드
     */
    private ErrorResponse makeErrorResponse(final ErrorCode errorCode, final String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    /**
     * BindException을 처리하는 메서드
     */
    private ResponseEntity<Object> handleExceptionInternal(final BindException e, final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    /**
     * BindException에서 발생한 유효성 검증 오류를 ErrorResponse로 변환하는 메서드
     */
    private ErrorResponse makeErrorResponse(final BindException e, final ErrorCode errorCode) {
        final List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }
}
