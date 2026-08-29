package bitnagil.infrastructure.auth.social.apple;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;

/**
 * 애플 로그인 후 받은 ID 토큰을 디코딩하여 페이로드를 추출합니다.
 */
public class TokenDecoder {

    public static <T> T decodePayload(String token, Class<T> targetClass) {
        String[] tokenParts = token.split("\\.");
        String payloadJWT = tokenParts[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(payloadJWT));
        ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.APPLE_TOKEN_DECODE_ERROR);
        }
    }
}
