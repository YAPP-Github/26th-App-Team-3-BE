package bitnagil.routineV2.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "루틴 완료 여부 갱신 DTO")
public class RoutineV2UpdateCompletionRequest {

    @Schema(description = "루틴 완료 여부를 갱신할 루틴 정보 리스트입니다.",
            required = true)
    @NotNull
    List<RoutineV2UpdateCompletionInfo> routineCompletionInfos;
}
