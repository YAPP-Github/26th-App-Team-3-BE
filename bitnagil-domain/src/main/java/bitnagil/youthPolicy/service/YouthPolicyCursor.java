package bitnagil.youthPolicy.service;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 커서 키를 인코딩/디코딩합니다. 커서 키("정렬키|plcyNo")를 base64url 불투명 토큰으로 감쌉니다.
 */
@Component
public class YouthPolicyCursor {

    public String encode(String cursorKey) {
        return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(cursorKey.getBytes(StandardCharsets.UTF_8));
    }

    public String decode(String cursorToken) {
        try {
            return new String(Base64.getUrlDecoder().decode(cursorToken), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_YOUTH_POLICY_CURSOR);
        }
    }
}