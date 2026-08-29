package bitnagil.auth.dto.response;

import bitnagil.user.domain.enums.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
