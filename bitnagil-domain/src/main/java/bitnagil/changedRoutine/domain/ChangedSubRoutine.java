package bitnagil.changedRoutine.domain;

import bitnagil.entity.BaseTimeEntity;
import bitnagil.entity.HistoryPk;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChangedSubRoutine extends BaseTimeEntity {

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "changed_sub_routine_id")),
        @AttributeOverride(name = "historySeq", column = @Column(name = "history_seq"))
    })
    private HistoryPk changedSubRoutinePk;

    @NotNull
    private String changedSubRoutineName;

    @NotNull
    private LocalDateTime historyStartDateTime;

    @NotNull
    private LocalDateTime historyEndDateTime;

    @NotNull
    private UUID changedRoutineId; // 변경된 루틴 ID

    @NotNull
    private Integer sortOrder; // 변경서브루틴의 순서를 나타내는 필드


    @Builder
    public ChangedSubRoutine(HistoryPk changedSubRoutinePk, String changedSubRoutineName,
        LocalDateTime historyStartDateTime, LocalDateTime historyEndDateTime, UUID changedRoutineId,
        Integer sortOrder) {
        this.changedSubRoutinePk = changedSubRoutinePk;
        this.changedSubRoutineName = changedSubRoutineName;
        this.historyStartDateTime = historyStartDateTime;
        this.historyEndDateTime = historyEndDateTime;
        this.changedRoutineId = changedRoutineId;
        this.sortOrder = sortOrder;
    }
}
