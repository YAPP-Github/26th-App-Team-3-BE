package bitnagil.global.response;

import bitnagil.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 공통 응답 DTO 클래스로 CustomResponseDto와 ErrorResponseDto의 부모 클래스 역할을 한다.
 */
@Getter
@RequiredArgsConstructor
public class ResponseDto {

    private final String code;
    private final String message;

    public static ResponseDto of(ErrorCode errorCode) {
        return new ResponseDto(errorCode.getCode(), errorCode.getMessage());
    }
}
