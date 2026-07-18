package bitnagil.badge.dto.response;

import bitnagil.badge.domain.enums.BadgeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BadgeResponse {

    @Schema(description = "뱃지 타입")
    private BadgeType badgeType;

    @Schema(description = "뱃지 타이틀", example = "체크 전문가")
    private String title;

    @Schema(description = "뱃지 설명", example = "큰 성취를 이루셨군요!")
    private String description;

    @Schema(description = "뱃지 이미지 URL")
    private String imageUrl;

    @Schema(description = "뱃지 획득 일시 (아직 획득한 뱃지가 없어 예비 전문가가 기본 표시된 경우 null)")
    private LocalDateTime acquiredAt;
}
