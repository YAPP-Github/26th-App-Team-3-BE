package bitnagil.infrastructure.auth.social.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

/**
 * AppleFeignClient에서 발생하는 예외를 핸들링하기 위한 설정 클래스입니다.
 */
public class AppleFeignClientConfiguration {

    @Bean
    public AppleFeignClientErrorDecoder appleFeignClientErrorDecoder() {
        return new AppleFeignClientErrorDecoder(new ObjectMapper());
    }
}
