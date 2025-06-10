package bitnagil.bitnagil_backend.infrastructure.jwt.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import bitnagil.bitnagil_backend.global.errorcode.JwtErrorCode;
import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 인가(Authorization) 과정에서 권한이 부족한 사용자가 보호된 리소스에 접근할 경우 호출되는 Handler입니다.
 * 403 Forbidden 응답과 함께 커스터마이징된 에러 메시지를 JSON 형식으로 반환합니다.
 *
 * Spring Security의 AccessDeniedHandler 구현체로,
 * Jwt 인증이 성공했지만 인가되지 않은 요청에 대해 동작합니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        CustomResponseDto<String> errorResponse = CustomResponseDto.from(JwtErrorCode.FORBIDDEN_USER,
            accessDeniedException.getMessage());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
