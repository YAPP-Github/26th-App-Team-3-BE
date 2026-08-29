package bitnagil.recommendedRoutine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RecommendedRoutineDto {

    private Long recommendedRoutineId;
    private String recommendedRoutineName;
    private String recommendedRoutineDescription;

    private List<RecommendedSubRoutineSearchResult> recommendedSubRoutineSearchResult;
}
