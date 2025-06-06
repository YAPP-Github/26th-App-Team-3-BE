package bitnagil.bitnagil_backend.global.response;

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
@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // errors가 비어있으면 JSON에 포함하지 않음
    private final List<ValidationError> errors;

    /**
     * BindException 발생 시 검증에 실패한 필드와 메시지를 담는 클래스
     * - 필드명과 그 필드에 대해 발생한 검증 실패 메시지를 담는 내부 클래스
     */
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}