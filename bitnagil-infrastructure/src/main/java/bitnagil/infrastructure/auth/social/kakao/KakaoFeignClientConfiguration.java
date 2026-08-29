package bitnagil.infrastructure.auth.social.kakao;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * KakaoFeignClient에서 발생하는 예외를 핸들링하기 위한 설정 클래스입니다.
 */
@Configuration
public class KakaoFeignClientConfiguration {

    @Bean
    public ErrorDecoder kakaoFeignErrorDecoder() {
        return new KakaoFeignClientErrorDecoder();
    }
}
