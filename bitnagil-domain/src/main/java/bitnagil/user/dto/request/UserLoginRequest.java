package bitnagil.user.dto.request;

import bitnagil.user.domain.enums.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "회원 로그인 요청 DTO")
public class UserLoginRequest {

    @Schema(description = "사용자 닉네임(애플 로그인 시에만 값을 설정한다.)", example = "홍길동")
    private String nickname;

    @Schema(description = "소셜 로그인 플랫폼입니다.",
            example = "KAKAO, APPLE",
            required = true)
    @NotNull
    private SocialType socialType;
}
