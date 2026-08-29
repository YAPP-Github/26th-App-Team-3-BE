package bitnagil.routineInfoV2.service;

import bitnagil.routineInfoV2.domain.RoutineInfoV2;
import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineType;
import bitnagil.user.domain.User;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 루틴 관련 엔티티 생성, 초기화 책임을 담당하는 클래스입니다.
 */
@Component
public class RoutineInfoV2Factory {

    // 신규 RoutineInfo 엔티티 생성 및 초기화
    public RoutineInfoV2 createNewRoutineInfo(String routineName, List<DayOfWeek> routineRepeatDay,
                                        LocalTime routineExecutionTime, LocalDate routineStartDate,
                                        LocalDate routineEndDate, RecommendedRoutineType recommendedRoutineType,
                                        User user) {
        return RoutineInfoV2.builder()
                .routineName(routineName)
                .routineRepeatDay(routineRepeatDay) // 온보딩은 반복일자를 설정하지 않는다.
                .routineExecutionTime(routineExecutionTime)
                .routineStartDate(routineStartDate)
                .routineEndDate(routineEndDate)
                .routineDeletedYn(false)
                .user(user)
                .recommendedRoutineType(recommendedRoutineType)
                .build();
    }
}

