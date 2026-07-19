package bitnagil.badge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "월별 획득 뱃지 조회 DTO")
public class MonthlyBadgeResponse {

    @Schema(description = "그 달을 대표하는 칭호. 획득 0개=예비 전문가, 1개=그 뱃지 이름, "
        + "2개=능숙한 전문가, 3개=완벽한 전문가", example = "능숙한 전문가")
    private String badgeTitle;

    @Schema(description = "칭호에 대응하는 대표 문구", example = "하나만 더 모으면 이번 달이 완성돼요!")
    private String badgeDescription;

    @Schema(description = "그 달 획득한 뱃지 아이콘 목록 (0개인 달은 예비 전문가 1건)")
    private List<BadgeResponse> badges;
}
