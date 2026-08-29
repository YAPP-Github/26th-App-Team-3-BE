package bitnagil.emotionMarble.dto.response;

import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "감정 구슬 조회 DTO")
public class EmotionMarbleTypeResponseV2 {
    @Schema(description = "감정 구슬 타입", example = "CALM")
    private EmotionMarbleType emotionMarbleType;

    @Schema(description = "감정 구슬 명칭", example = "평온함")
    private String emotionMarbleName;

    @Schema(description = "감정 구슬 이미지 URL (홈/구슬 선택 화면 이미지 다름)", example = "https://example.com/image/calm.png")
    private String imageUrl;

    @Schema(description = "감정 구슬 홈 화면 메시지", example = "오늘은 평온하군요~")
    private String emotionMarbleHomeMessage;
}
