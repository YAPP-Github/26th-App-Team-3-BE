package bitnagil.bitnagil_backend.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 사용자 정보 응답을 매핑하기 위한 DTO 클래스입니다.
 *
 * Kakao API에서 사용자 정보를 요청했을 때 전달되는 JSON 응답 중
 * 사용자 ID와 kakao_account 정보를 매핑합니다.
 */
@Getter
@NoArgsConstructor
public class KakaoUserInfo {

    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
}