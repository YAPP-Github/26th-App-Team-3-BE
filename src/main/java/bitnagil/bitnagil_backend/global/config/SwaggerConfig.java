package bitnagil.bitnagil_backend.global.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 설정 클래스
 * Swagger를 사용하여 API 문서를 자동으로 생성합니다.
 * Swagger UI는 http://localhost:8080/swagger-ui/index.html에서 확인할 수 있습니다.
 */

@OpenAPIDefinition(servers = {
        @Server(url = "https://dev.bitnagil.com", description = "개발 서버 도메인"),
        @Server(url = "https://www.bitnagil.com", description = "운영 서버 도메인"),
        @Server(url = "http://localhost:8080", description = "로컬 서버 도메인")},
        info = @io.swagger.v3.oas.annotations.info.Info(title = "Bitnagil App",
                description = "Bitnagil api명세",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("Bitnagil API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {

        String accessKey = "Access Token";
        String refreshKey = "Refresh Token";

        /**
         * Access Token과 Refresh Token에 대한 보안 스키마 정의
         */

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(accessKey)
                .addList(refreshKey);

        SecurityScheme accessToken = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityScheme refreshToken = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization-refresh");

        Components components = new Components()
                .addSecuritySchemes(accessKey, accessToken)
                .addSecuritySchemes(refreshKey, refreshToken);

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
