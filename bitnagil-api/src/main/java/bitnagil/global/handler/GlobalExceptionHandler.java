package bitnagil.global.handler;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.global.response.ErrorResponseDto;
import bitnagil.infrastructure.slack.SlackMessageOptions;
import bitnagil.infrastructure.slack.SlackService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 전역 예외 처리기
 * 애플리케이션 전역에서 발생하는 예외를 처리합니다.
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackService slackService;

    /**
     * 커스텀 예외를 처리하는 메서드
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(final CustomException e) {
        log.error("CustomException 발생 - errorName: {}, code: {}, status: {}, message: {}", e.getErrorCode(), e.getErrorCode().getCode(), e.getErrorCode().getHttpStatus(), e.getErrorCode().getMessage());
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
     * 유효한 요청이지만 리소스가 없을 때
     * 예를 들어, 데이터베이스에서 요청한 리소스가 존재하지 않을 때 발생하는 예외를 처리하는 메서드
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("NoResourceFound Exception occurred. message={}, className={}", e.getMessage(), e.getClass().getName());
        final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * 요청 파라미터가 누락되었을 때 발생하는 예외를 처리하는 메서드
     * @RequestParam으로 필수 파라미터를 받았는데 클라이언트가 해당 파라미터를 보내지 않았을 때 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameter Exception occurred. parameterName={}, message={}", e.getParameterName(), e.getMessage());
        final ErrorCode errorCode = ErrorCode.REQUIRED_PARAMETER_NOT_FOUND;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * @RequestParam, @PathVariable 등에서 타입이 일치하지 않을 때 발생하는 예외를 처리하는 메서드
     * 예를 들어, 쿼리 파라미터로 숫자를 기대했는데 문자열이 넘어왔을 때 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatch Exception occurred. message={}", e.getMessage());
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * @ModelAttribute를 사용하는 요청에서 바인딩 실패(타입 불일치, 필수 파라미터 누락 등) 시 발생하는 예외를 처리하는 메서드
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(final BindException e) {
        log.error("BindException occurred. message={}, className={}", e.getMessage(), e.getClass().getName());
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        String message = getBindExceptionMessage(e);
        sendSlackMessage(e, errorCode, message);
        return handleExceptionInternal(errorCode, e, message);
    }


    /**
     * @Valid가 붙은 @RequestBody DTO 객체의 유효성 검증 실패 시 발생하는 예외를 처리하는 메서드
     * 즉, JSON 형태로 넘어온 요청 본문이 DTO 제약 조건(@NotBlank, @Size 등)을 위반했을 때 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
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
     * PathVariable로 요청한 리소스를 찾을 수 없을 때 발생하는 예외를 처리하는 메서드
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException e) {
        log.warn("handleMissingPathVariable", e);
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        sendSlackMessage(e, errorCode);
        return handleExceptionInternal(errorCode, e);
    }

    /**
     * 요청한 핸들러를 찾을 수 없을 때 발생하는 예외를 처리하는 메서드
     * 잘못된 URL로 요청했을 때 발생
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e){
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
     * ErrorCode + Exception + message 으로 처리
     */
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, Exception e, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponseDto.of(errorCode, e, message));
    }

    /**
     * Slack 메시지를 전송하는 메서드
     * 예외 발생 시 Slack에 에러 로그를 전송합니다.
     * 예외와 에러코드를 받아 Slack에 메시지를 전송
     */
    private void sendSlackMessage(Exception e, ErrorCode errorCode) {
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("에러 로그", e.getMessage() != null ? e.getMessage() : errorCode.getMessage());
        String title = "에러 코드: " + errorCode.getCode() + "\n"
                + "상태 코드: " + errorCode.getHttpStatus().value() + "\n"
                + "메시지: " + errorCode.getMessage();
        SlackMessageOptions options = SlackMessageOptions.builder()
                .title(title)
                .data(messageMap)
                .build();
        slackService.sendMessage(options);
    }

    /**
     * Slack 메시지를 전송하는 메서드
     * 예외 발생 시 Slack에 에러 로그를 전송합니다.
     * 예외와 에러코드를 받아 Slack에 메시지를 전송
     */
    private void sendSlackMessage(Exception e, ErrorCode errorCode, String message) {
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("에러 로그", e.getMessage());
        String title = "에러 코드: " + errorCode.getCode() + "\n"
                + "상태 코드: " + errorCode.getHttpStatus().value() + "\n"
                + "메시지: " + errorCode.getMessage() + "\n"
                + "추가 메시지: " + message;
        SlackMessageOptions options = SlackMessageOptions.builder()
                .title(title)
                .data(messageMap)
                .build();
        slackService.sendMessage(options);
    }

    /**
     * @Valid 처리된 DTO의 메세지를 가져오기 위한 메서드
     */
    private String getBindExceptionMessage(BindException e) {
        // 메세지가 존재하는 경우에
        if (e.getFieldError() != null && e.getFieldError().getDefaultMessage() != null) {
            return e.getFieldError().getDefaultMessage();
        }

        // 메세지가 존재하지 않는 경우에는 어떤 값들이 잘못되었는지 확인
        return e.getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", ")) + " 값들이 정확하지 않습니다.";
    }
}
