package bitnagil.changedRoutine.domain;

import bitnagil.changedRoutine.domain.enums.ChangedDivCode;
import bitnagil.entity.BaseTimeEntity;
import bitnagil.entity.HistoryPk;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * 규칙적인 류틴에 대해 일시적인 변경이 발생한 루틴에 대해 관리하는 엔티티입니다.
 * 시간 변경, 내일 미루기, 오늘만 루틴 삭제 등 기존 루틴을 수정,삭제하는 것이 아닌 일시적인 변경시에 해당 엔티티에 이력을 쌓습니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChangedRoutine extends BaseTimeEntity {

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "changed_routine_id")),
        @AttributeOverride(name = "historySeq", column = @Column(name = "history_seq"))
    })
    private HistoryPk changedRoutinePk;

    @NotNull
    private String changedRoutineName; // 변경된 루틴 이름

    @NotNull
    private LocalTime changedExecutionTime; // 변경된 루틴 실행 시간

    @NotNull
    private LocalDate originalRoutineDate; // 원본 루틴 날짜(원본 루틴에서 해당 날짜는 루틴이 뜨지 않도록 한다.)

    @NotNull
    private LocalDate changedRoutineDate; // 변경된 루틴 날짜(실제 루틴이 실행될 날짜)

    @NotNull
    private LocalDateTime historyStartDateTime; // 이력 시작일시

    @NotNull
    private LocalDateTime historyEndDateTime; // 이력 종료일시

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private ChangedDivCode changedDivCode; // 변경 구분 코드 (시간 변경, 내일 미루기, 오늘만 루틴 삭제 등)

    @NotNull
    private Long userId;

    private UUID routineId;

    @Builder
    public ChangedRoutine(HistoryPk changedRoutinePk, String changedRoutineName, LocalTime changedExecutionTime,
        LocalDate originalRoutineDate, LocalDate changedRoutineDate, LocalDateTime historyStartDateTime,
        LocalDateTime historyEndDateTime, Long userId, UUID routineId, ChangedDivCode changedDivCode) {
        this.changedRoutinePk = changedRoutinePk;
        this.changedRoutineName = changedRoutineName;
        this.changedExecutionTime = changedExecutionTime;
        this.originalRoutineDate = originalRoutineDate;
        this.changedRoutineDate = changedRoutineDate;
        this.historyStartDateTime = historyStartDateTime;
        this.historyEndDateTime = historyEndDateTime;
        this.changedDivCode = changedDivCode;
        this.userId = userId;
        this.routineId = routineId;
    }

    public void updateChangedDivCode(ChangedDivCode changedDivCode) {
        this.changedDivCode = changedDivCode;
    }

}
