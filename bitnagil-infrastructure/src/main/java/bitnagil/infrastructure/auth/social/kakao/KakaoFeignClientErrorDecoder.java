package bitnagil.infrastructure.auth.social.kakao;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Kakao FeignClient 호출 시 예외를 처리하는 핸들러입니다.
 */
@Slf4j
@RequiredArgsConstructor
public class KakaoFeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = null;
        if (response != null && response.body() != null) {
            try {
                body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("Error reading response body", e);
            }
        }

        log.error("카카오 소셜 로그인 Feign API 호출 중 오류가 발생했습니다. body: {}", body);

        if (methodKey.contains("getUserInfo")) {
            return new CustomException(ErrorCode.KAKAO_USER_INFO_FAILED);
        } else if (methodKey.contains("logout")) {
            return new CustomException(ErrorCode.KAKAO_LOGOUT_FAILED);
        } else if (methodKey.contains("unlink")) {
            return new CustomException(ErrorCode.KAKAO_UNLINK_FAILED);
        }

        throw new CustomException(ErrorCode.KAKAO_FEIGN_CALL_FAILED);
    }
}
