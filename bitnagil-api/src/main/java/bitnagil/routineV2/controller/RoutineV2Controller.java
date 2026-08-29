package bitnagil.routineV2.controller;

import bitnagil.routineInfoV2.dto.request.RoutineInfoV2UpdateRequest;
import bitnagil.routineV2.dto.request.RoutineV2RegisterRequest;
import bitnagil.routineV2.dto.request.RoutineV2UpdateCompletionRequest;
import bitnagil.routineV2.dto.response.RoutineV2SearchResponse;
import bitnagil.routineV2.dto.response.RoutineV2SearchResultDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.routineV2.controller.spec.RoutineV2Spec;
import bitnagil.routineV2.service.RoutineV2Service;
import bitnagil.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v2/routines")
public class RoutineV2Controller implements RoutineV2Spec {

    private final RoutineV2Service routineV2Service;

    // 회원이 보유한 특정 기간(start_date, end_date)의 루틴을 조회하는 API입니다.
    @GetMapping
    public CustomResponseDto<RoutineV2SearchResponse> getRoutines(@CurrentUser User user,
                                                                  @RequestParam @NotNull LocalDate startDate,
                                                                  @RequestParam @NotNull LocalDate endDate) {
        return CustomResponseDto.from(routineV2Service.getRoutines(user, startDate, endDate));
    }

    // 루틴 단건 조회
    @GetMapping("{routineId}")
    public CustomResponseDto<RoutineV2SearchResultDto> getRoutine(@CurrentUser User user,
                                                                  @PathVariable Long routineId) {
        return CustomResponseDto.from(routineV2Service.getRoutine(user, routineId));
    }

    // 루틴을 새롭게 등록하는 API 입니다.
    @PostMapping("")
    public CustomResponseDto<Object> registerRoutine(@CurrentUser User user, @RequestBody RoutineV2RegisterRequest request) {
        routineV2Service.registerRoutineV2(user, request);

        return CustomResponseDto.from(null);
    }

    // 루틴 당일(오늘)만 삭제하는 API 입니다.
    @DeleteMapping("/day/{routineId}")
    public CustomResponseDto<Object> deleteRoutineByDay(@CurrentUser User user, @PathVariable Long routineId) {
        routineV2Service.deleteRoutineByDay(user, routineId);

        return CustomResponseDto.from(null);
    }

    @PatchMapping("")
    public CustomResponseDto<Object> updateRoutineInfo(@CurrentUser User user, @RequestBody RoutineInfoV2UpdateRequest request) {
        routineV2Service.updateRoutineInfo(user, request);

        return CustomResponseDto.from(null);
    }

    /*
     * 루틴 완료 여부를 갱신하는 API 입니다.
     * 멱등성이 보장되는 업데이트 API이므로 PUT Method를 사용했습니다.
     */
    @PutMapping("/completions")
    public CustomResponseDto<Object> updateRoutineCompletionStatus(
        @CurrentUser User user, @RequestBody RoutineV2UpdateCompletionRequest request) {

        routineV2Service.updateRoutineCompletionStatus(user, request);

        return CustomResponseDto.from(null);
    }

}
