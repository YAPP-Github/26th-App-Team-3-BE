package bitnagil.routineV2.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class RoutineV2SearchResponse {
    @Schema(description = "날짜(LocalDate: 2025-08-01)와 같은 형태를 key로 가지는 루틴 목록 Map입니다. Swagger에서는 additionalProp1처럼 보일 수 있습니다.")
    private Map<LocalDate, RoutineData> routines; // 날짜별 루틴 목록

    @Getter
    @AllArgsConstructor
    @Builder
    public static class RoutineData {
        @Schema(description = "날짜별 루틴 목록")
        private List<RoutineV2SearchResultDto> routineList;

        @Schema(description = "해당 날짜 모든 루틴이 완료되었는지 여부")
        private boolean allCompleted;

        public void setAllCompleted(boolean allCompleted) {
            this.allCompleted = allCompleted;
        }
    }
}
