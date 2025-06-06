package bitnagil.bitnagil_backend.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 설정 클래스
 * Swagger를 사용하여 API 문서를 자동으로 생성합니다.
 * Swagger UI는 http://localhost:8080/swagger-ui/index.html에서 확인할 수 있습니다.
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("Bitnagil API")
                .description("Bitnagil API reference for developers")
                .version("1.0");
    }
}
