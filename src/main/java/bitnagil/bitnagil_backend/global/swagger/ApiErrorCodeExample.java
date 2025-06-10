package bitnagil.bitnagil_backend.global.swagger;

import bitnagil.bitnagil_backend.global.errorcode.ErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExample {

    /**
     * 단일 에러 코드 예시를 지정합니다.
     * @return 에러 코드 클래스
     */
    ErrorCode value();
}