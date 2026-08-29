package bitnagil.onboarding.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "온보딩 루틴 등록 요청 DTO")
@AllArgsConstructor
public class RegistrationRoutinesRequest {

    @Schema(description = "추천 루틴 ID 목록", required = true, example = "[1, 2, 3]")
    List<Long> recommendedRoutineIds;
}
