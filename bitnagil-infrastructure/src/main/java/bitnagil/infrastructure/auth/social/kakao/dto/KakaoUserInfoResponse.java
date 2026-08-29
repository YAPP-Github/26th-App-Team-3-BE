package bitnagil.infrastructure.auth.social.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 사용자 정보 응답을 매핑하기 위한 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {

    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
}
