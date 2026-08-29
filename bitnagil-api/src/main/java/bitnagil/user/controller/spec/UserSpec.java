package bitnagil.user.controller.spec;

import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiTags;
import bitnagil.user.dto.response.UserInfoResponse;
import bitnagil.user.dto.response.UserOnboardingResponse;
import bitnagil.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 유저 API 스펙 정의
 */
@Tag(name = ApiTags.USER)
public interface UserSpec {

    @Operation(summary = "유저 정보를 조회합니다.")
    CustomResponseDto<UserInfoResponse> getUserInfo(User user);

    @Operation(summary = "유저의 온보딩 정보를 조회합니다.")
    CustomResponseDto<UserOnboardingResponse> getUserOnboarding(User user);
}
