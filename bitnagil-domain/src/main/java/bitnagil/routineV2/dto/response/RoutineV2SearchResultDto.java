package bitnagil.routineV2.dto.response;

import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RoutineV2SearchResultDto {
    @Schema(example = "1")
    private String routineId; // RoutineV2의 ID(V2에서는 String으로 파싱해서 전달)
    @Schema(example = "물마시기")
    private String routineName; // 루틴 이름
    @Schema(example = "[MONDAY, WEDNESDAY, FRIDAY]")
    private List<DayOfWeek> repeatDay; // 반복 요일
    @Schema(example = "08:30:00")
    private LocalTime executionTime; // 루틴 실행 시간
    @Schema(example = "2025-08-01")
    private LocalDate routineDate; // 루틴 일자
    @Schema(example = "true")
    private Boolean routineCompleteYn;
    @Schema(example = "[물 10초만에 마시기, 물 20초만에 마시기]")
    private List<String> subRoutineNames;
    @Schema(example = "[true, false]")
    private List<Boolean> subRoutineCompleteYn;
    @Schema(example = "WAKE_UP")
    private RecommendedRoutineType recommendedRoutineType;
    @Schema(example = "true")
    private Boolean routineDeletedYn; // 루틴 삭제 여부
    @Schema(example = "2025-08-15")
    private LocalDate routineStartDate; // 루틴 시작 일자
    @Schema(example = "2025-08-31")
    private LocalDate routineEndDate; // 루틴 종료 일자
}
