package bitnagil.user.dto.response;

import bitnagil.user.domain.enums.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 *  로그인 후 토큰 관련 JSON 정보를 담은 클래스입니다.
 */
@Getter
@AllArgsConstructor
@Builder
public class UserTokenResponse {
    @NotEmpty
    private String accessToken;

    @NotEmpty
    private String refreshToken;

    @NotEmpty
    private Role role;
}
