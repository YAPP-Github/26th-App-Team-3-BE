package bitnagil.infrastructure.vworld;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 실제 vworld 지오코더 API를 호출하는 통합 테스트입니다.
 * Feign 클라이언트 배선과 응답 역직렬화까지 실제 네트워크로 검증합니다.
 *
 * 실행: VWORLD_API_KEY=<key> ./gradlew :bitnagil-infrastructure:test \
 *          --tests 'bitnagil.infrastructure.vworld.GeocodingServiceRealApiTest'
 */
@EnabledIfEnvironmentVariable(named = "VWORLD_API_KEY", matches = ".+") // 환경변수 VWORLD_API_KEY가 없으면 테스트를 건너뜀
@SpringBootTest(
    classes = GeocodingServiceRealApiTest.TestApplication.class, // 테스트용 스프링 부트 애플리케이션 설정 클래스
    properties = "vworld.api-key=${VWORLD_API_KEY}"
)
class GeocodingServiceRealApiTest {

    @Autowired
    private GeocodingService geocodingService;

    @Test
    @DisplayName("종로구 좌표는 시군구코드 11110을 반환한다")
    void resolveSeoulJongno() {
        // 서울특별시 종로구 (경복궁 인근)
        Optional<String> code = geocodingService.resolveSigunguCode(37.5729, 126.9794);

        assertThat(code).contains("11110");
    }

    @Test
    @DisplayName("대한민국 밖 좌표는 빈 값을 반환한다")
    void resolveOutOfKorea() {
        // 대한민국 주소로 매칭될 수 없는 좌표
        Optional<String> code = geocodingService.resolveSigunguCode(0.0, 0.0);

        assertThat(code).isEmpty();
    }

    // 테스트를 위한 스프링 부트 애플리케이션 설정 클래스
    @SpringBootConfiguration
    @EnableFeignClients(clients = VworldGeocoderClient.class)
    @Import(GeocodingService.class)
    @ImportAutoConfiguration({
        FeignAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class
    })
    static class TestApplication {
    }
}
