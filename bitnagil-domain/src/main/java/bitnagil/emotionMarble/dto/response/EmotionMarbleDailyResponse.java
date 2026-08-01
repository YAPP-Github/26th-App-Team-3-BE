package bitnagil.emotionMarble.dto.response;

import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "일별 감정 구슬 조회 DTO")
public class EmotionMarbleDailyResponse {

    @Schema(description = "감정 구슬을 선택한 날짜", example = "2026-07-01")
    private LocalDate date;

    @Schema(description = "감정 구슬 타입", example = "CALM")
    private EmotionMarbleType emotionMarbleType;

    @Schema(description = "감정 구슬 명칭", example = "평온함")
    private String emotionMarbleName;

    @Schema(description = "활동일지용 감정 구슬 이미지 URL", example = "https://example.com/activity_log_calm.png")
    private String imageUrl;
}
