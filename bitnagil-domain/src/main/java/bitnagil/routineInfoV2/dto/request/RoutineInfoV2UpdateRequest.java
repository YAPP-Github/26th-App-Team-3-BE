package bitnagil.routineInfoV2.dto.request;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import bitnagil.routineV2.domain.enums.UpdateApplyDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "루틴 정보 수정 DTO")
public class RoutineInfoV2UpdateRequest {

    @Schema(description = "루틴 ID 값입니다.",
            example = "3",
            required = true)
    @NotNull
    private String routineId;

    @Schema(description = "오늘/내일 중 반영 시작할 날짜",
            example = "TODAY",
            required = true)
    @NotNull
    private UpdateApplyDate updateApplyDate;

    @Schema(description = "루틴 이름입니다.",
        example = "아침 준비",
        required = true)
    @NotNull
    private String routineName;

    @Schema(description = "반복 요일에 대한 리스트입니다. (반복요일이 없으면 당일 루틴입니다.)",
        example = "[\"MONDAY\", \"FRIDAY\"]",
        required = true)
    @NotNull
    private List<DayOfWeek> repeatDay;

    @Schema(description = "루틴 시작 일자입니다.",
        example = "2025-08-01")
    private LocalDate routineStartDate;

    @Schema(description = "루틴 시작 일자입니다.",
        example = "2025-08-31")
    private LocalDate routineEndDate;

    @Schema(description = "루틴 시작 시간입니다.",
        example = "08:15:00",
        required = true)
    @NotNull
    private LocalTime executionTime;

    @Schema(description = "세부 루틴 이름에 대한 리스트입니다.",
        example = "[\"손 씻기\", \"세수 하기\", \"양치 하기\"]")
    private List<String> subRoutineName;
}
