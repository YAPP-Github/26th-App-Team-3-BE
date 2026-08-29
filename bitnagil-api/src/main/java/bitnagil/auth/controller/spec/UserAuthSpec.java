package bitnagil.auth.controller.spec;

import bitnagil.auth.dto.request.UserLoginRequest;
import bitnagil.auth.dto.response.UserTokenResponse;
import bitnagil.errorcode.ErrorCode;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiErrorCodeExamples;
import bitnagil.global.swagger.ApiTags;
import bitnagil.user.domain.User;
import bitnagil.user.dto.request.UserAgreementsRequest;
import bitnagil.user.dto.request.UserWithdrawalRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 유저 인증 API 스펙 정의
 */
@Tag(name = ApiTags.USER_AUTH)
public interface UserAuthSpec {

    @Operation(summary = "소셜회원가입 및 로그인을 수행하고 토큰을 발행합니다.")
    @ApiErrorCodeExamples({
        ErrorCode.KAKAO_FEIGN_CALL_FAILED, ErrorCode.KAKAO_USER_INFO_FAILED, ErrorCode.APPLE_TOKEN_DECODE_ERROR,
        ErrorCode.APPLE_FEIGN_CALL_FAILED, ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.APPLE_UNLINK_PENDING
    })
    @Parameters({
        @Parameter(
            name = "SocialAccessToken",
            description = "소셜로그인 플랫폼에서 발급해준 access token 입니다.(Bearer를 붙히지 않습니다.)",
            required = true,
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
            in = ParameterIn.HEADER
        )
    })
    CustomResponseDto<UserTokenResponse> login(UserLoginRequest userLoginRequest, String socialAccessToken);

    @Operation(summary = "유저가 로그아웃합니다. 반환 정보는 없습니다.")
    @ApiErrorCodeExamples({
        ErrorCode.KAKAO_FEIGN_CALL_FAILED, ErrorCode.KAKAO_LOGOUT_FAILED
    })
    CustomResponseDto<Object> logout(User user);

    @Operation(summary = "토큰 재발급 요청으로 토큰 관련 정보를 반환합니다.")
    @ApiErrorCodeExamples({
        ErrorCode.INVALID_JWT_TOKEN, ErrorCode.NOT_FOUND_USER
    })
    @Parameters({
        @Parameter(
            name = "Refresh-Token",
            description = "서버에서 발급해준 refresh token 입니다.(Bearer를 붙히지 않습니다.)",
            required = true,
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
            in = ParameterIn.HEADER
        )
    })
    CustomResponseDto<UserTokenResponse> reissueToken(String refreshToken);

    @Operation(summary = "소셜로그인으로 연결된 유저가 회원탈퇴합니다. 반환 정보는 없습니다.")
    @ApiErrorCodeExamples({
        ErrorCode.KAKAO_FEIGN_CALL_FAILED, ErrorCode.KAKAO_UNLINK_FAILED, ErrorCode.APPLE_FEIGN_CALL_FAILED
    })
    CustomResponseDto<Object> withdrawal(UserWithdrawalRequest userWithdrawalRequest, User user);

    @Operation(summary = "유저가 최초 회원가입 시 약관 동의를 처리합니다.")
    @ApiErrorCodeExamples({
        ErrorCode.NOT_FOUND_USER, ErrorCode.AGREEMENT_NOT_ACCEPTED
    })
    CustomResponseDto<Void> agreements(UserAgreementsRequest userAgreementsRequest, User user);
}
