package bitnagil.bitnagil_backend.global.handler;

import bitnagil.bitnagil_backend.global.SlackService;
import bitnagil.bitnagil_backend.global.errorcode.ErrorCode;
import bitnagil.bitnagil_backend.global.exception.CustomException;
import bitnagil.bitnagil_backend.global.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

/**
 * 전역 예외 처리기
 * 애플리케이션 전역에서 발생하는 예외를 처리합니다.
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final SlackService slackService;

    /**
     * 커스텀 예외를 처리하는 메서드
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(final CustomException e) {
        log.error("handleCustomException",e.toString(), e);
        final ErrorCode errorCode = e.getErrorCode();
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode);
    }

    /**
     * 적절하지 않은 인자를 받았을 때 발생하는 예외를 처리하는 메서드
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * @Valid가 붙은 @RequestBody DTO 객체의 유효성 검증 실패 시 발생하는 예외를 처리하는 메서드
     * 즉, JSON 형태로 넘어온 요청 본문이 DTO 제약 조건(@NotBlank, @Size 등)을 위반했을 때 발생
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        log.warn("handleMethodArgumentNotValid", e);
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * @Validated가 붙은 @RequestParam, @PathVariable, @ModelAttribute의 유효성 검증 실패 시 발생하는 예외를 처리하는 메서드
     * 즉, 쿼리 파라미터나 경로 변수, 폼 데이터의 제약 조건(@NotBlank, @Size 등)을 위반했을 때 발생
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException e) {
        log.warn("handleConstraintViolation", e);
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * @ModelAttribute를 사용하는 요청에서 바인딩 실패(타입 불일치, 필수 파라미터 누락 등) 시 발생하는 예외를 처리하는 메서드
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(final BindException e) {
        log.warn("handleBindException", e);
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * PathVariable로 요청한 리소스를 찾을 수 없을 때 발생하는 예외를 처리하는 메서드
     */
    @Override
    public ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        log.warn("handleMissingPathVariable", e);
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * 요청한 핸들러를 찾을 수 없을 때 발생하는 예외를 처리하는 메서드
     * 예를 들어, 잘못된 URL로 요청했을 때 발생
     */
    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        log.warn("handleNoHandlerFoundException", e);
        final ErrorCode errorCode = ErrorCode.NOT_FOUND_HANDLER;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * Exception.class 예외를 처리하는 메서드
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(final Exception e) {
        log.warn("handleAllException", e);
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode);
    }

    /**
     * ErrorCode만으로 처리
     */
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponseDto.from(errorCode));
    }

    /**
     * ErrorCode + Exception 으로 처리
     */
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, Exception e) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponseDto.of(errorCode, e));
    }

    /**
     * Slack 메시지를 전송하는 메서드
     * 예외 발생 시 Slack에 에러 로그를 전송합니다.
     */
    private void sendSlackMessage(Exception e, ErrorCode errorCode) {
        HashMap<String, String> message = new HashMap<>();
        message.put("에러 로그", e.getMessage());
        String title = "에러 코드: " + errorCode.getCode() + "\n"
                + "상태 코드: " + errorCode.getHttpStatus().value() + "\n"
                + "메시지: " + errorCode.getMessage();
        slackService.sendMessage(title, message);
    }
}
