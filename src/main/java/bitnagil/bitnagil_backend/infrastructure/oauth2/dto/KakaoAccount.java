package bitnagil.bitnagil_backend.infrastructure.oauth2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 동의항목의 정보를 받는 클래스입니다.
 */
@Getter
@NoArgsConstructor
public class KakaoAccount {
    private String email;
}
