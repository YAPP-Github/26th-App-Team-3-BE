package bitnagil.routineV2.service;

import bitnagil.routineInfoV2.domain.RoutineInfoV2;
import bitnagil.routineV2.domain.RoutineV2;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 루틴 관련 엔티티 생성, 초기화 책임을 담당하는 클래스입니다.
 */
@Component
public class RoutineV2Factory {

    // 신규 Routine 엔티티 생성 및 초기화
    public RoutineV2 createNewRoutine(LocalDate routineDate, Boolean routineCompleteYn, List<String> subRoutineNames,
                                      List<Boolean> subRoutineCompleteYn, RoutineInfoV2 routineInfo) {
        return RoutineV2.builder()
                .routineDate(routineDate)
                .routineCompleteYn(routineCompleteYn)
                .subRoutineNames(subRoutineNames)
                .subRoutineCompleteYn(subRoutineCompleteYn)
                .routineInfo(routineInfo)
                .build();
    }
}

