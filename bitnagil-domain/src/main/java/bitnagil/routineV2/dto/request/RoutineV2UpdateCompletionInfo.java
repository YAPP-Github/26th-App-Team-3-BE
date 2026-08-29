package bitnagil.routineV2.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoutineV2UpdateCompletionInfo {

    @Schema(description = "루틴 완료 여부를 갱신할 루틴 ID 값입니다.",
            example = "4",
            required = true)
    @NotNull
    private String routineId;

    @Schema(description = "메인 루틴의 완료 여부입니다.",
            example = "true",
            required = true)
    @NotNull
    private Boolean routineCompleteYn;

    @Schema(description = "서브루틴 완료 여부 리스트입니다.",
            example = "[true, false, true]",
            required = true)
    @NotNull
    private List<Boolean> subRoutineCompleteYn;
}
