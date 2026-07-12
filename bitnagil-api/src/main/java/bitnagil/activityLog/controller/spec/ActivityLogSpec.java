package bitnagil.activityLog.controller.spec;

import bitnagil.activityLog.dto.response.MonthlyActivityLogResponse;
import bitnagil.errorcode.ErrorCode;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiErrorCodeExamples;
import bitnagil.global.swagger.ApiTags;
import bitnagil.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Tag(name = ApiTags.ACTIVITY_LOG)
public interface ActivityLogSpec {

    @Operation(summary = "월별 활동일지 조회",
        description = "지정한 연·월 기준으로 회원의 활동일지를 조회합니다.\n\n"
            + "- badges: 해당 월에 획득한 활동 뱃지 목록 (아직 하나도 획득한 적이 없으면 \"예비 전문가\" 1건 기본 표시)\n"
            + "- emotionMarbles: 해당 월에 선택한 일별 감정 구슬 목록")
    @Parameters({
        @Parameter(name = "year", description = "조회 연도", required = true, example = "2026"),
        @Parameter(name = "month", description = "조회 월 (1~12)", required = true, example = "7")
    })
    @ApiErrorCodeExamples({ErrorCode.REQUIRED_PARAMETER_NOT_FOUND, ErrorCode.INVALID_PARAMETER})
    CustomResponseDto<MonthlyActivityLogResponse> getMonthlyActivityLog(
        User user,
        @Min(2000) @Max(9999) int year,
        @Min(1) @Max(12) int month);
}
