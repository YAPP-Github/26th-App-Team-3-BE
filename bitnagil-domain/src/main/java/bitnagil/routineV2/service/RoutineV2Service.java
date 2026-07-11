package bitnagil.routineV2.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import bitnagil.badge.domain.enums.BadgeTriggerAction;
import bitnagil.badge.event.BadgeTriggerEvent;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.routineInfoV2.dto.request.RoutineInfoV2UpdateRequest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bitnagil.routineInfoV2.domain.RoutineInfoV2;
import bitnagil.routineInfoV2.repository.RoutineInfoV2Repository;
import bitnagil.routineInfoV2.service.RoutineInfoV2Factory;
import bitnagil.routineV2.domain.RoutineV2;
import bitnagil.routineV2.domain.enums.UpdateApplyDate;
import bitnagil.routineV2.dto.request.RoutineV2RegisterRequest;
import bitnagil.routineV2.dto.request.RoutineV2UpdateCompletionInfo;
import bitnagil.routineV2.dto.request.RoutineV2UpdateCompletionRequest;
import bitnagil.routineV2.dto.response.RoutineV2SearchResponse;
import bitnagil.routineV2.dto.response.RoutineV2SearchResultDto;
import bitnagil.routineV2.repository.RoutineV2Repository;
import bitnagil.user.domain.User;
import lombok.RequiredArgsConstructor;

/**
 * [v2] 루틴 관련된 서비스 로직을 담은 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class RoutineV2Service {

    private final RoutineInfoV2Repository routineInfoV2Repository;
    private final RoutineInfoV2Factory routineInfoV2Factory;
    private final RoutineV2Factory routineV2Factory;
    private final RoutineV2Repository routineV2Repository;
    private final RoutineV2Mapper routineV2Mapper;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 회원이 보유한 특정 기간(start_date, end_date)의 루틴을 조회하는 메서드입니다.
     */
    @Transactional(readOnly = true)
    public RoutineV2SearchResponse getRoutines(User user, LocalDate startDate, LocalDate endDate) {
        return queryRoutines(user, startDate, endDate);
    }

    /**
     * 회원이 보유한 루틴 단건 조회
     */
    @Transactional(readOnly = true)
    public RoutineV2SearchResultDto getRoutine(User user, Long routineId) {
        RoutineV2 routineV2 = routineV2Repository.findByUserAndRoutineId(user, routineId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROUTINE));

        return routineV2Mapper.toRoutineV2SearchResultDto(routineV2);
    }

    /**
     * 루틴 정보를 등록하면서 루틴 시작, 종료일자를 기반으로 루틴 내역을 생성
     */
    @Transactional
    public void registerRoutineV2(User user, RoutineV2RegisterRequest request) {

        if(request.getRoutineStartDate().isAfter(request.getRoutineEndDate())) {
            throw new CustomException(ErrorCode.INVALID_ROUTINE_DATE_RANGE);
        }

        LocalDate today = LocalDate.now();

        // repeatDay가 비어 있으면 빈 리스트, 아니면 요청값 사용
        List<DayOfWeek> repeatDays = request.getRepeatDay().isEmpty() ? List.of() : request.getRepeatDay();

        // 루틴 정보 등록
        RoutineInfoV2 routineInfo = routineInfoV2Factory.createNewRoutineInfo(
            request.getRoutineName(),
            repeatDays,
            request.getExecutionTime(),
            request.getRoutineStartDate(),
            request.getRoutineEndDate(),
            request.getRecommendedRoutineType(),
            user);

        routineInfoV2Repository.save(routineInfo);

        // 루틴을 생성할 날짜 목록 생성
        createRoutinesMatchedRepeatDayWithinPeriod(request.getRepeatDay().isEmpty()
            ? List.of(today) // 당일 루틴
            : generateRoutineDatesWithinPeriod(
            request.getRoutineStartDate(),
            request.getRoutineEndDate(),
            request.getRepeatDay()), request.getSubRoutineName(), routineInfo);
    }

    // 루틴 오늘만 삭제 메서드
    @Transactional
    public void deleteRoutineByDay(User user, Long routineId) {
        RoutineV2 routineV2 = routineV2Repository.findByUserAndRoutineId(user, routineId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROUTINE));

        routineV2Repository.deletePhysicallyById(routineV2.getRoutineId()); // 물리 삭제
    }

    // 루틴 정보 수정 메서드
    @Transactional
    public void updateRoutineInfo(User user, RoutineInfoV2UpdateRequest request) {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Long routineId = Long.valueOf(request.getRoutineId());

        RoutineV2 routineV2 = routineV2Repository.findByUserAndRoutineId(user, routineId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROUTINE));

        RoutineInfoV2 routineInfoV2 = routineInfoV2Repository.findById(routineV2.getRoutineInfo().getRoutineInfoId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROUTINE_INFO));

        // 요첨받은 루틴 정보가 기존 루틴 정보와 동일할 경우
        if (!isChangedRoutineInfo(request, routineInfoV2, routineV2)) return;

        // 변경사항을 적용할 변경날짜를 설정
        LocalDate changedDate = request.getUpdateApplyDate().equals(UpdateApplyDate.TODAY) ? today : tomorrow;

        // 기존 루틴에서 수정날짜 이후 데이터는 물리 삭제
        routineV2Repository.deleteByRoutineDateBetweenAndRoutineInfo(
            changedDate, routineInfoV2.getRoutineEndDate(), routineInfoV2.getRoutineInfoId());

        // 기존 루틴 정보의 종료일자를 업데이트
        routineInfoV2.updateRoutineEndDate(changedDate.minusDays(1));

        // 변경날짜부터의 새로운 루틴 등록 request 변환
        RoutineV2RegisterRequest routineV2RegisterRequest = RoutineV2RegisterRequest.builder()
            .routineName(request.getRoutineName())
            .repeatDay(request.getRepeatDay())
            .routineStartDate(changedDate)
            .routineEndDate(request.getRoutineEndDate())
            .executionTime(request.getExecutionTime())
            .subRoutineName(request.getSubRoutineName())
            .recommendedRoutineType(routineInfoV2.getRecommendedRoutineType())
            .build();

        // 변경날짜부터의 새로운 루틴 등록
        registerRoutineV2(user, routineV2RegisterRequest);
    }

    // 루틴 정보에서 변경된 부분이 있는지 검증
    private boolean isChangedRoutineInfo(RoutineInfoV2UpdateRequest request, RoutineInfoV2 routineInfoV2, RoutineV2 routineV2) {
        return !routineInfoV2.getRoutineName().equals(request.getRoutineName()) ||
            !routineInfoV2.getRoutineRepeatDay().equals(request.getRepeatDay()) ||
            !routineInfoV2.getRoutineExecutionTime().equals(request.getExecutionTime()) ||
            !routineInfoV2.getRoutineStartDate().equals(request.getRoutineStartDate()) ||
            !routineInfoV2.getRoutineEndDate().equals(request.getRoutineEndDate()) ||
            !routineV2.getSubRoutineNames().equals(request.getSubRoutineName());
    }

    private void createRoutinesMatchedRepeatDayWithinPeriod(
        List<LocalDate> targetDates, List<String> request, RoutineInfoV2 routineInfoV2) {

        // 서브 루틴 완료 여부 리스트 생성
        List<Boolean> subRoutineCompleteYn = request.stream()
            .map(completeYn -> false)
            .toList();

        // 위 날짜 목록을 바탕으로 루틴 생성
        List<RoutineV2> routinesToRegister = targetDates.stream()
            .map(routineDate -> routineV2Factory.createNewRoutine(
                routineDate,
                false,
                request,
                subRoutineCompleteYn,
                routineInfoV2
            ))
            .toList();

        routineV2Repository.saveAll(routinesToRegister);
    }

    // 루틴 완료 여부를 업데이트 하는 메서드
    @Transactional
    public void updateRoutineCompletionStatus(User user, RoutineV2UpdateCompletionRequest request) {
        boolean anyCompleted = false;
        for (RoutineV2UpdateCompletionInfo info : request.getRoutineCompletionInfos()) {
            Long routineId = Long.valueOf(info.getRoutineId());
            RoutineV2 routineV2 = routineV2Repository.findByUserAndRoutineId(user, routineId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROUTINE));

            // 루틴, 서브루틴 완료 여부 갱신
            routineV2.updateRoutineCompleteYn(info.getRoutineCompleteYn(), info.getSubRoutineCompleteYn());

            if (Boolean.TRUE.equals(info.getRoutineCompleteYn())) {
                anyCompleted = true;
            }
        }

        if (anyCompleted) {
            eventPublisher.publishEvent(
                new BadgeTriggerEvent(user.getUserId(), BadgeTriggerAction.ROUTINE_COMPLETE, YearMonth.now()));
        }
    }

    /**
     * 날짜 범위에서 주어진 요일(repeatDays)에 해당하는 날짜만 반환
     */
    private List<LocalDate> generateRoutineDatesWithinPeriod(
        LocalDate startDate, LocalDate endDate, List<DayOfWeek> repeatDays) {

        List<LocalDate> routineDatesToRegister = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (repeatDays.contains(date.getDayOfWeek())) {
                routineDatesToRegister.add(date);
            }
        }
        return routineDatesToRegister;
    }

    // 특정 기간 보유 루틴 조회
    private RoutineV2SearchResponse queryRoutines(User user, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, RoutineV2SearchResponse.RoutineData> response = new HashMap<>();

        List<RoutineV2> routineList = routineV2Repository.findByUserAndDateRange(user, startDate, endDate);

        for (RoutineV2 routineV2 : routineList) {
            LocalDate date = routineV2.getRoutineDate();
            RoutineV2SearchResultDto routineSearchResultDto = routineV2Mapper.toRoutineV2SearchResultDto(routineV2);

            // 날짜별 RoutineData 생성 혹은 가져오기
            response.computeIfAbsent(date, key -> RoutineV2SearchResponse.RoutineData.builder()
                    .routineList(new ArrayList<>())
                    .allCompleted(true) // 초기값 true
                    .build());

            RoutineV2SearchResponse.RoutineData routineData = response.get(date);

            // 리스트에 추가
            routineData.getRoutineList().add(routineSearchResultDto);

            // 하나라도 완료 안 된 루틴이 있으면 false로 변경
            if (!routineSearchResultDto.getRoutineCompleteYn()) {
                routineData.setAllCompleted(false);
            }
        }

        // 정렬 처리
        response.values().forEach(data ->
                data.getRoutineList().sort(Comparator.comparing(RoutineV2SearchResultDto::getExecutionTime))
        );

        return routineV2Mapper.toRoutineV2SearchResponse(response);
    }
}

