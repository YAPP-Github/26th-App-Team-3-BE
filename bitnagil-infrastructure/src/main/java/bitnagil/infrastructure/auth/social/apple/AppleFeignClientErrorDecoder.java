package bitnagil.infrastructure.auth.social.apple;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign 클라이언트 호출 중 오류가 발생했을 때 예외를 변환하는 ErrorDecoder 구현입니다.
 */
@Slf4j
@RequiredArgsConstructor
public class AppleFeignClientErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = null;
        try {
            if (response != null && response.body() != null) {
                body = new String(response.body().asInputStream().readAllBytes());

                if (body.trim().startsWith("{")) {
                    try {
                        Object parsed = objectMapper.readValue(body, Object.class);
                        log.error("Feign 오류 응답 (JSON): {}", parsed);
                    } catch (IOException jsonEx) {
                        log.warn("JSON 파싱 실패: 원문 출력 -> {}", body, jsonEx);
                    }
                } else {
                    log.error("Feign 오류 응답 (TEXT): {}", body);
                }
            }
        } catch (IOException e) {
            log.error("Feign 응답 바디 읽기 실패", e);
        }

        log.error("애플 소셜 로그인 Feign API 호출 중 오류가 발생했습니다. body: {}", body);
        return new CustomException(ErrorCode.APPLE_FEIGN_CALL_FAILED);
    }
}
