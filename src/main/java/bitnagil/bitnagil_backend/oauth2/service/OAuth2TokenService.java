package bitnagil.bitnagil_backend.oauth2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import bitnagil.bitnagil_backend.oauth2.dto.KakaoTokenResponse;
import bitnagil.bitnagil_backend.oauth2.dto.KakaoUserInfo;

/**
 * 카카오 OAuth2 인증을 위한 토큰 발급 및 사용자 정보 조회를 담당하는 서비스입니다.
 *
 * RestTemplate을 사용하여 카카오 인증 서버와 통신하며,
 * 추후 애플 인증 추가 예정
 */
@Service
public class OAuth2TokenService {

    @Value("${spring.security.oauth2.client.provider.kakao-provider.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.kakao-provider.user-info-uri}")
    private  String USER_INFO_URI;

    private final RestTemplate restTemplate;

    public OAuth2TokenService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public KakaoTokenResponse getKakaoToken(String clientId, String redirectUri, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
            TOKEN_URI, request, KakaoTokenResponse.class);

        return response.getBody();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
            USER_INFO_URI,
            HttpMethod.GET,
            request,
            KakaoUserInfo.class
        );

        return response.getBody();
    }
}


