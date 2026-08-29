package bitnagil.emotionMarble.dto.response;

import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "감정 구슬 등록 응답 DTO")
public class RegisterEmotionMarbleResponse {

    @Schema(description = "추천 루틴 목록")
    private List<RecommendedRoutineDto> recommendedRoutines;

}
