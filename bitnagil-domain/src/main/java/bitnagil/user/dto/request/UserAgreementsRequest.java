package bitnagil.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "회원 약관 동의 요청 DTO")
@AllArgsConstructor
public class UserAgreementsRequest {

    @Schema(description = "서비스 이용약관 동의", example = "true", required = true)
    private Boolean agreedToTermsOfService; // 서비스 이용약관 동의
    @Schema(description = "개인정보 수집 동의", example = "true", required = true)
    private Boolean agreedToPrivacyPolicy; // 개인정보 수집 동의
    @Schema(description = "14세 이상 여부 동의", example = "true", required = true)
    private Boolean isOverFourteen; // 14세 이상 여부
}
