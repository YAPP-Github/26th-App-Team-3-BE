package bitnagil.user.dto.response;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 유저 정보를 담는 Response 클래스입니다.
 */
@Getter
@AllArgsConstructor
@Builder
public class UserInfoResponse {

    @NotEmpty
    private String nickname;
}
