package bitnagil.recommendedRoutine.dto.response;

import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class RecommendedRoutineSearchResponse {

    @Schema(description = "추천 루틴 타입을 key로 가지는 루틴 목록 Map입니다. Swagger에서는 additionalProp1처럼 보일 수 있습니다.")
    Map<String, List<RecommendedRoutineSearchResult>> recommendedRoutines;
    @Schema(description = "감정 구슬 타입")
    EmotionMarbleType emotionMarbleType;
}
