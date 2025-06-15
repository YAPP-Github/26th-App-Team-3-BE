package bitnagil.bitnagil_backend.auth.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import bitnagil.bitnagil_backend.global.errorcode.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import bitnagil.bitnagil_backend.global.response.CustomResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출되는 EntryPoint.
 * 401 Unauthorized 응답과 함께 JSON 형태의 에러 메시지를 반환합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        log.error("Unauthorized access to URL: {}, message: {}", request.getRequestURI(), authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        CustomResponseDto<String> errorResponse = CustomResponseDto.from(ErrorCode.UNAUTHENTICATED_USER,
            authException.getMessage());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
