package bitnagil.changedRoutine.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import bitnagil.changedRoutine.domain.ChangedRoutine;
import bitnagil.changedRoutine.domain.ChangedSubRoutine;
import bitnagil.changedRoutine.domain.enums.ChangedDivCode;
import bitnagil.entity.HistoryPk;
import bitnagil.user.domain.User;
import bitnagil.utils.TimeUtils;

@Service
public class ChangedRoutineFactory {

    // 유저 초기 온보딩 시 추천 루틴을 등록할 때 변경 루틴에 저장
    public ChangedRoutine createChangedRoutineForToday(
        User user, String RoutineName, LocalTime executionTime, LocalDate today, LocalDateTime now) {

        return ChangedRoutine.builder()
            .changedRoutinePk(new HistoryPk(UUID.randomUUID(), 1L))
            .changedRoutineName(RoutineName)
            .changedExecutionTime(executionTime)
            .originalRoutineDate(today) // 원본 루틴 날짜는 현재 날짜로 설정
            .changedRoutineDate(today) // 변경된 루틴 날짜도 현재 날짜로 설정
            .historyStartDateTime(now)
            .historyEndDateTime(TimeUtils.END_DATE_TIME)
            .userId(user.getUserId())
            .changedDivCode(ChangedDivCode.ONBOARDING)
            .build();
    }

    // 유저 초기 온보딩 시 추천 루틴을 등록할 때 변경 서브루틴에 저장
    public ChangedSubRoutine createChangedSubRoutineForToday(
        int sortOrder, String subRoutineName, LocalDateTime now, ChangedRoutine changedRoutine) {

        return ChangedSubRoutine.builder()
            .changedSubRoutinePk(new HistoryPk(UUID.randomUUID(), 1L))
            .changedSubRoutineName(subRoutineName)
            .historyStartDateTime(now)
            .historyEndDateTime(TimeUtils.END_DATE_TIME)
            .changedRoutineId(changedRoutine.getChangedRoutinePk().getId())
            .sortOrder(sortOrder + 1) // 1부터 시작
            .build();
    }
}

