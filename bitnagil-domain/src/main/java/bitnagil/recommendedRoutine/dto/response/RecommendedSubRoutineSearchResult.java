package bitnagil.recommendedRoutine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class RecommendedSubRoutineSearchResult {
    @Schema(description = "추천 서브 루틴 ID", example = "1")
    private Long recommendedSubRoutineId;
    @Schema(description = "추천 서브 루틴 이름", example = "물 2L 마시기")
    private String recommendedSubRoutineName;
}
