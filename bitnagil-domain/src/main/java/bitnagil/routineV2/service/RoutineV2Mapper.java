package bitnagil.routineV2.service;

import bitnagil.routineV2.domain.RoutineV2;
import bitnagil.routineV2.dto.response.RoutineV2SearchResponse;
import bitnagil.routineV2.dto.response.RoutineV2SearchResultDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

/**
 * 루틴 관련해서 DB에서 조회해오거나 가공된 데이터를 DTO로 변환하는 Mapper 클래스입니다.
 */
@Component
public class RoutineV2Mapper {

    public RoutineV2SearchResultDto toRoutineV2SearchResultDto(RoutineV2 routine){
        return RoutineV2SearchResultDto.builder()
                .routineId(String.valueOf(routine.getRoutineId()))
                .routineName(routine.getRoutineInfo().getRoutineName())
                .repeatDay(routine.getRoutineInfo().getRoutineRepeatDay())
                .executionTime(routine.getRoutineInfo().getRoutineExecutionTime())
                .routineDate(routine.getRoutineDate())
                .routineCompleteYn(routine.getRoutineCompleteYn())
                .subRoutineNames(routine.getSubRoutineNames())
                .subRoutineCompleteYn(routine.getSubRoutineCompleteYn())
                .recommendedRoutineType(routine.getRoutineInfo().getRecommendedRoutineType())
                .routineDeletedYn(routine.getRoutineInfo().getRoutineDeletedYn())
                .routineStartDate(routine.getRoutineInfo().getRoutineStartDate())
                .routineEndDate(routine.getRoutineInfo().getRoutineEndDate())
                .build();
    }

    public RoutineV2SearchResponse toRoutineV2SearchResponse(Map<LocalDate, RoutineV2SearchResponse.RoutineData> response) {
        return RoutineV2SearchResponse.builder()
                .routines(response)
                .build();
    }
}

