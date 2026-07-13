package bitnagil.activityLog.controller.spec;

import bitnagil.badge.dto.response.BadgeResponse;
import bitnagil.emotionMarble.dto.response.EmotionMarbleDailyResponse;
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
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Tag(name = ApiTags.ACTIVITY_LOG)
public interface ActivityLogSpec {

    @Operation(summary = "월별 획득 뱃지 조회",
        description = "지정한 연·월에 획득한 활동 뱃지 목록을 조회합니다. (최신 획득순)\n\n"
            + "해당 월에 획득한 뱃지가 하나도 없으면 \"예비 전문가\" 1건을 기본으로 반환합니다. (acquiredAt=null)")
    @Parameters({
        @Parameter(name = "year", description = "조회 연도", required = true, example = "2026"),
        @Parameter(name = "month", description = "조회 월 (1~12)", required = true, example = "7")
    })
    @ApiErrorCodeExamples({ErrorCode.REQUIRED_PARAMETER_NOT_FOUND, ErrorCode.INVALID_PARAMETER})
    CustomResponseDto<List<BadgeResponse>> getMonthlyBadges(
        User user,
        @Min(2000) @Max(9999) int year,
        @Min(1) @Max(12) int month);

    @Operation(summary = "기간별 감정 구슬 조회",
        description = "지정한 기간(startDate ~ endDate)에 선택한 일별 감정 구슬 목록을 조회합니다.\n\n"
            + "감정 구슬을 선택한 날짜만 포함되며(하루 최대 1개), 날짜 오름차순으로 정렬됩니다.")
    @Parameters({
        @Parameter(name = "startDate", description = "조회 시작일", required = true, example = "2026-07-01"),
        @Parameter(name = "endDate", description = "조회 종료일", required = true, example = "2026-07-31")
    })
    @ApiErrorCodeExamples({ErrorCode.REQUIRED_PARAMETER_NOT_FOUND, ErrorCode.INVALID_PARAMETER})
    CustomResponseDto<List<EmotionMarbleDailyResponse>> getDailyEmotionMarbles(
        User user,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate);
}
