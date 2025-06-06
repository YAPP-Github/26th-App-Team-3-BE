package bitnagil.bitnagil_backend.global.errorcode;

import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * ErrorCode 인터페이스는 모든 에러 코드가 구현해야 하는 기본 인터페이스입니다.
 * 이 인터페이스를 구현함으로써, 각 에러 코드 클래스는 공통적인 메서드나 속성을 정의할 수 있습니다.
 */
public interface ErrorCode {
    /**
     * 에러 코드의 이름을 반환합니다.
     * 에러 코드는 구현체 클래스의 이름의 앞 2글자와 순번을 합쳐서 생성합니다.
     * 예: "CO001", "US002" 등
     */
    String getCode();

    /**
     * 에러 코드의 HTTP 상태 코드를 반환합니다.
     */
    HttpStatus getHttpStatus();

    /**
     * 에러 코드의 메시지를 반환합니다.
     */
    String getMessage();

    // 공통 메서드 1: Throwable 기반 메시지 생성
    default String getMessage(Throwable throwable) {
        return getMessage(getMessage() + " - " + throwable.getMessage());
    }

    // 공통 메서드 2: message override 시 기본 메시지 fallback
    default String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(getMessage());
    }
}