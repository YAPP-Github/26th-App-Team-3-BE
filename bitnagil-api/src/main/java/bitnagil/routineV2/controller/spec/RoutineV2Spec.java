package bitnagil.routineV2.controller.spec;

import bitnagil.global.annotation.CurrentUser;
import bitnagil.errorcode.ErrorCode;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiErrorCodeExamples;
import bitnagil.global.swagger.ApiTags;
import bitnagil.routineV2.dto.request.RoutineV2RegisterRequest;
import bitnagil.routineInfoV2.dto.request.RoutineInfoV2UpdateRequest;
import bitnagil.routineV2.dto.request.RoutineV2UpdateCompletionRequest;
import bitnagil.routineV2.dto.response.RoutineV2SearchResponse;
import bitnagil.routineV2.dto.response.RoutineV2SearchResultDto;
import bitnagil.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = ApiTags.ROUTINEV2)
public interface RoutineV2Spec {

    @Operation(summary = "루틴 조회",
            description = "회원이 특정 기간에 보유한 루틴에 대한 정보를 조회합니다.")
    @Parameters({
            @Parameter(name = "startDate", description = "조회 시작일", required = true, example = "2025-07-01"),
            @Parameter(name = "endDate", description = "조회 종료일", required = true, example = "2025-07-13")
    })
    CustomResponseDto<RoutineV2SearchResponse> getRoutines(User user, @NotNull LocalDate startDate, @NotNull LocalDate endDate);

    @Operation(summary = "회원이 보유한 루틴을 단건 조회합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_ROUTINE})
    CustomResponseDto<RoutineV2SearchResultDto> getRoutine(User user, Long routineId);


    @Operation(summary = "루틴 정보 등록 및 루틴 시작, 종료일자 사이에서 반복요일에 해당하는 날짜로 루틴 데이터를 생성합니다.")
    CustomResponseDto<Object> registerRoutine(User user, RoutineV2RegisterRequest request);

    @Operation(summary = "루틴 정보를 업데이트합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_ROUTINE, ErrorCode.NOT_FOUND_ROUTINE_INFO})
    CustomResponseDto<Object> updateRoutineInfo(User user, RoutineInfoV2UpdateRequest request);

    @Operation(summary = "여러 루틴의 완료 여부를 갱신합니다. (여러 루틴의 완료 여부를 리스트로 만들어 요청하는 방식입니다.)")
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_ROUTINE})
    CustomResponseDto<Object> updateRoutineCompletionStatus(
        @CurrentUser User user, @RequestBody RoutineV2UpdateCompletionRequest request);

    @Operation(summary = "오늘만 루틴을 삭제합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_ROUTINE})
    CustomResponseDto<Object> deleteRoutineByDay(User user, Long routineId);
}
