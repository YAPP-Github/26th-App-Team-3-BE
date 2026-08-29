package bitnagil.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "회원 탈퇴 요청 DTO")
public class UserWithdrawalRequest {

    @Schema(description = "서비스 탈퇴 사유입니다.", example = "루틴을 수행하기 어려워요.")
    private String reasonOfWithdrawal;
}
