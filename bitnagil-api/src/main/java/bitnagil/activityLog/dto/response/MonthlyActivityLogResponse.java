package bitnagil.activityLog.dto.response;

import bitnagil.badge.dto.response.BadgeResponse;
import bitnagil.emotionMarble.dto.response.EmotionMarbleDailyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "월별 활동일지 조회 DTO")
public class MonthlyActivityLogResponse {

    @Schema(description = "조회 연도", example = "2026")
    private int year;

    @Schema(description = "조회 월 (1~12)", example = "7")
    private int month;

    @Schema(description = "해당 월에 획득한 활동 뱃지 목록. 아직 뱃지를 하나도 획득한 적이 없으면 \"예비 전문가\" 1건이 기본 표시됩니다.")
    private List<BadgeResponse> badges;

    @Schema(description = "해당 월의 일별 감정 구슬 목록 (감정 구슬을 선택한 날짜만 포함, 날짜 오름차순)")
    private List<EmotionMarbleDailyResponse> emotionMarbles;
}
