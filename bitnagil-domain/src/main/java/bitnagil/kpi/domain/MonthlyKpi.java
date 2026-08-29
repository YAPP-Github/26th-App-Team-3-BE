package bitnagil.kpi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 월별 성과 KPI 집계 결과를 저장하는 엔티티입니다.
 * 매월 1일 스케줄러가 전월 데이터를 집계하여 한 건 저장합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MonthlyKpi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "target_month", unique = true, nullable = false)
    private LocalDate targetMonth;

    @Column(precision = 5, scale = 2)
    private BigDecimal routineCompletionWithinSevenDaysRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal monthlyRoutineActiveUserRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal routineRegistrationRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal outingRoutineCompletionRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal positiveEmotionRate;

    @Builder
    public MonthlyKpi(LocalDate targetMonth,
        BigDecimal routineCompletionWithinSevenDaysRate,
        BigDecimal monthlyRoutineActiveUserRate,
        BigDecimal routineRegistrationRate,
        BigDecimal outingRoutineCompletionRate,
        BigDecimal positiveEmotionRate) {
        this.targetMonth = targetMonth;
        this.routineCompletionWithinSevenDaysRate = routineCompletionWithinSevenDaysRate;
        this.monthlyRoutineActiveUserRate = monthlyRoutineActiveUserRate;
        this.routineRegistrationRate = routineRegistrationRate;
        this.outingRoutineCompletionRate = outingRoutineCompletionRate;
        this.positiveEmotionRate = positiveEmotionRate;
    }
}
