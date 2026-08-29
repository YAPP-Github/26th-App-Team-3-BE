package bitnagil.emotionMarble.dto.request;

import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "감정구슬 등록 요청 DTO")
public class RegisterEmotionMarbleRequest {

    @Schema(description = "감정구술 enum 값",
            example = "CALM", required = true)
    @NotNull
    private EmotionMarbleType emotionMarbleType;
}
