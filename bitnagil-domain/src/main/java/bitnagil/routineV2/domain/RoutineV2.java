package bitnagil.routineV2.domain;

import java.time.LocalDate;
import java.util.List;

import bitnagil.utils.BooleanListConverter;
import bitnagil.utils.StringListConverter;
import bitnagil.routineInfoV2.domain.RoutineInfoV2;
import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * 추후 v2를 대비한 일자별 루틴을 관리하는 엔티티 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE routine_v2 SET deleted_at = NOW() WHERE routine_id = ?")
@Where(clause = "deleted_at IS NULL")
public class RoutineV2 extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routineId; // 일일 루틴 ID

    @NotNull
    private LocalDate routineDate; // 루틴 일자

    @NotNull
    private Boolean routineCompleteYn; // 루틴 완료 여부

    @NotNull
    @Convert(converter = StringListConverter.class)
    private List<String> subRoutineNames; // 서브 루틴 이름 리스트

    @NotNull
    @Convert(converter = BooleanListConverter.class)
    private List<Boolean> subRoutineCompleteYn; // 서브 루틴 완료 여부 리스트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_info_id")
    private RoutineInfoV2 routineInfo; // 루틴 정보

    @Builder
    public RoutineV2(LocalDate routineDate, Boolean routineCompleteYn, List<String> subRoutineNames,
        List<Boolean> subRoutineCompleteYn, RoutineInfoV2 routineInfo) {
        this.routineDate = routineDate;
        this.routineCompleteYn = routineCompleteYn;
        this.subRoutineNames = subRoutineNames;
        this.subRoutineCompleteYn = subRoutineCompleteYn;
        this.routineInfo = routineInfo;
    }

    // 루틴 완료 여부 갱신
    public void updateRoutineCompleteYn(Boolean routineCompleteYn, List<Boolean> subRoutineCompleteYn) {
        this.routineCompleteYn = routineCompleteYn;
        this.subRoutineCompleteYn = subRoutineCompleteYn;
    }
}
