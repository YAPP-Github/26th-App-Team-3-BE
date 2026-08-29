package bitnagil.recommendedRoutine.dto.response;

import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineLevel;
import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Jacksonized
public class RecommendedRoutineSearchResult {
    @Schema(description = "추천 루틴 ID", example = "1")
    private Long recommendedRoutineId;
    @Schema(description = "추천 루틴 이름", example = "물마시기")
    private String recommendedRoutineName;
    @Schema(description = "추천 루틴 설명", example = "하루에 물을 많이 마셔보아요")
    private String recommendedRoutineDescription;
    @Schema(description = "추천 루틴 난이도", example = "LEVEL1")
    private RecommendedRoutineLevel recommendedRoutineLevel;
    @Schema(description = "추천 루틴 수행 시간", example = "08:00:00")
    private LocalTime executionTime;
    @Schema(description = "추천 루틴 타입", example = "WAKE_UP")
    private RecommendedRoutineType recommendedRoutineType;
    private List<RecommendedSubRoutineSearchResult> recommendedSubRoutineSearchResult;
}
