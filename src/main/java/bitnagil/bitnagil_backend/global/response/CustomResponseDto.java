package bitnagil.bitnagil_backend.global.response;

import bitnagil.bitnagil_backend.global.errorcode.CommonErrorCode;
import bitnagil.bitnagil_backend.global.errorcode.ErrorCode;
import lombok.Getter;

/**
 * 커스텀 응담 DTO 클래스
 * 컨트롤러 단에서 사용되는 응답 DTO로, 데이터와 에러 코드(응답 코드)를 포함한다.
 */
@Getter
public class CustomResponseDto<T> extends ResponseDto {

    private final T data;

    private CustomResponseDto(ErrorCode code, T data) {
        super(code.getCode(), code.getMessage());
        this.data = data;
    }

    // 기본 성공 응답: CommonErrorCode.OK를 기본값으로 사용
    public static <T> CustomResponseDto<T> from(T data) {
        return new CustomResponseDto<>(CommonErrorCode.OK, data);
    }

    // 에러 코드(응답 코드)와 데이터를 함께 사용하는 응답
    public static <T> CustomResponseDto<T> from(ErrorCode code, T data) {
        return new CustomResponseDto<>(code, data);
    }
}
