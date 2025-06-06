package bitnagil.bitnagil_backend.global.errorcode;

import org.springframework.http.HttpStatus;

/**
 * ErrorCode 인터페이스는 모든 에러 코드가 구현해야 하는 기본 인터페이스입니다.
 * 이 인터페이스를 구현함으로써, 각 에러 코드 클래스는 공통적인 메서드나 속성을 정의할 수 있습니다.
 */
public interface ErrorCode {
    /**
     * 에러 코드의 이름을 반환합니다.
     *
     * @return 에러 코드 이름
     */
    String name();

    /**
     * 에러 코드의 HTTP 상태 코드를 반환합니다.
     *
     * @return HTTP 상태 코드
     */
    HttpStatus getHttpStatus();

    /**
     * 에러 코드의 메시지를 반환합니다.
     *
     * @return 에러 메시지
     */
    String getMessage();
}