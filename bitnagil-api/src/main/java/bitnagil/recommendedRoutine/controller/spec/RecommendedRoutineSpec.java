package bitnagil.recommendedRoutine.controller.spec;

import bitnagil.errorcode.ErrorCode;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiErrorCodeExamples;
import bitnagil.global.swagger.ApiTags;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineSearchResponse;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineSearchResult;
import bitnagil.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = ApiTags.RECOMMENDED_ROUTINE)
public interface RecommendedRoutineSpec {

    @Operation(summary = "추천 루틴을 조회합니다.")
    @ApiErrorCodeExamples({
            ErrorCode.NOT_FOUND_USER
    })
    CustomResponseDto<RecommendedRoutineSearchResponse> searchRecommendedRoutines(User user);

    @Operation(summary = "추천 루틴 단건을 조회합니다.")
    @ApiErrorCodeExamples({
            ErrorCode.NOT_FOUND_RECOMMENDED_ROUTINE
    })
    @Parameters({
            @Parameter(name = "recommendedRoutineId", description = "추천 루틴 ID", required = true, example = "1")
    })
    public CustomResponseDto<RecommendedRoutineSearchResult> searchRecommendedRoutine(Long recommendedRoutineId);
}
