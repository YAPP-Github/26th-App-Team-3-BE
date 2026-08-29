package bitnagil.routineInfoV2.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import bitnagil.utils.DayOfWeekConverter;
import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineType;
import bitnagil.user.domain.User;
import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * 추후 v2를 대비한 루틴에 대한 정보를 관리하는 엔티티 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE routine_infov2 SET deleted_at = NOW() WHERE routine_info_id = ?")
@Where(clause = "deleted_at IS NULL")
public class RoutineInfoV2 extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routineInfoId; // 루틴 정보 ID

    @NotNull
    private String routineName; // 루틴 이름

    @NotNull
    @Convert(converter = DayOfWeekConverter.class)
    private List<DayOfWeek> routineRepeatDay; // 루틴 반복 요일

    @NotNull
    private LocalTime routineExecutionTime; // 루틴 실행 시간

    @NotNull
    private LocalDate routineStartDate; // 루틴 시작 일자

    @NotNull
    private LocalDate routineEndDate; // 루틴 종료 일자

    @NotNull
    private Boolean routineDeletedYn; // 루틴 삭제 여부

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private RecommendedRoutineType recommendedRoutineType; // 추천 루틴 타입

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 루틴의 주체인 유저

    @Builder
    public RoutineInfoV2(String routineName, List<DayOfWeek> routineRepeatDay, LocalTime routineExecutionTime,
        LocalDate routineStartDate, LocalDate routineEndDate, Boolean routineDeletedYn, User user,
        RecommendedRoutineType recommendedRoutineType) {
        this.routineName = routineName;
        this.routineRepeatDay = routineRepeatDay;
        this.routineExecutionTime = routineExecutionTime;
        this.routineStartDate = routineStartDate;
        this.routineEndDate = routineEndDate;
        this.routineDeletedYn = routineDeletedYn;
        this.user = user;
        this.recommendedRoutineType = recommendedRoutineType;
    }

    public void updateRoutineEndDate(LocalDate routineEndDate) {
        this.routineEndDate = routineEndDate;
    }

    public void updateRoutineDeletedYn(Boolean routineDeletedYn) {
        this.routineDeletedYn = routineDeletedYn;
    }
}
