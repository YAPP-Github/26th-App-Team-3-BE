package bitnagil.userOnboardingInfo.controller.spec;

import bitnagil.errorcode.ErrorCode;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiErrorCodeExamples;
import bitnagil.global.swagger.ApiTags;
import bitnagil.userOnboardingInfo.dto.response.UserOnboardingInfoSearchResponse;
import bitnagil.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = ApiTags.ONBOARDING)
public interface UserOnboardingInfoSpec {

    @Operation(summary = "유저의 온보딩 정보를 조회합니다.")
    @ApiErrorCodeExamples({
            ErrorCode.NOT_FOUND_USER_ONBOARDING_INFO
    })
    public CustomResponseDto<UserOnboardingInfoSearchResponse> getUserOnboardingInfo(User user);

}
