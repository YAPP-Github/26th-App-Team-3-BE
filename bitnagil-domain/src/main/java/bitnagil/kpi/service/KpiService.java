package bitnagil.kpi.service;

import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import bitnagil.emotionMarble.repository.EmotionMarbleRepository;
import bitnagil.kpi.domain.MonthlyKpi;
import bitnagil.kpi.event.MonthlyKpiSavedEvent;
import bitnagil.kpi.repository.KpiQueryRepository;
import bitnagil.kpi.repository.MonthlyKpiRepository;
import bitnagil.user.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 월별 KPI 집계 서비스입니다.
 * 전월 기준 6개 지표를 계산하여 MonthlyKpi로 저장합니다.
 */
@Service
@RequiredArgsConstructor
public class KpiService {

    private static final List<EmotionMarbleType> POSITIVE_EMOTION_TYPES = List.of(
        EmotionMarbleType.CALM,
        EmotionMarbleType.VITALITY,
        EmotionMarbleType.SATISFACTION
    );

    private final MonthlyKpiRepository monthlyKpiRepository;
    private final KpiQueryRepository kpiQueryRepository;
    private final UserRepository userRepository;
    private final EmotionMarbleRepository emotionMarbleRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 지정한 월에 대한 KPI를 계산하여 저장합니다.
     * 이미 해당 월 데이터가 있으면 저장하지 않습니다.
     */
    @Transactional
    public void calculateAndSave(YearMonth targetMonth) {
        LocalDate targetMonthDate = targetMonth.atDay(1);
        if (monthlyKpiRepository.existsByTargetMonth(targetMonthDate)) {
            return;
        }

        LocalDate start = targetMonth.atDay(1);
        LocalDate end = targetMonth.atEndOfMonth();
        LocalDateTime monthStartDt = start.atStartOfDay();
        LocalDateTime monthEndDt = end.atTime(23, 59, 59);

        BigDecimal kpi1 = calculateRoutineCompletionWithin7DaysRate(targetMonth, monthStartDt, monthEndDt);
        BigDecimal kpi2 = calculateMonthlyRoutineActiveUserRate(start, end, monthEndDt);
        long mauCount = countMonthlyActiveUsers(start, end, monthStartDt, monthEndDt);
        BigDecimal kpi3 = calculateRoutineRegistrationRate(start, end, monthStartDt, monthEndDt, mauCount);
        BigDecimal kpi4 = calculateOutingRoutineCompletionRate(start, end, mauCount);
        BigDecimal kpi6 = calculatePositiveEmotionRate(start, end);

        MonthlyKpi entity = MonthlyKpi.builder()
            .targetMonth(targetMonthDate)
            .routineCompletionWithinSevenDaysRate(kpi1)
            .monthlyRoutineActiveUserRate(kpi2)
            .routineRegistrationRate(kpi3)
            .outingRoutineCompletionRate(kpi4)
            .positiveEmotionRate(kpi6)
            .build();

        monthlyKpiRepository.save(entity);
        eventPublisher.publishEvent(new MonthlyKpiSavedEvent(entity));
    }

    /**
     * 지표 1: 가입 후 7일 이내 루틴 1회 이상 완료 비율 (%)
     */
    private BigDecimal calculateRoutineCompletionWithin7DaysRate(
        YearMonth targetMonth,
        LocalDateTime monthStartDt,
        LocalDateTime monthEndDt) {
        long newUserCount = userRepository.countByCreatedAtBetween(monthStartDt, monthEndDt);
        if (newUserCount == 0) {
            return BigDecimal.ZERO;
        }
        List<Long> v1 = kpiQueryRepository.findUserIdsWithRoutineCompletionWithin7DaysV1(monthStartDt, monthEndDt);
        List<Long> v2 = kpiQueryRepository.findUserIdsWithRoutineCompletionWithin7DaysV2(monthStartDt, monthEndDt);
        Set<Long> distinctUsers = new HashSet<>(v1);
        distinctUsers.addAll(v2);
        long count = distinctUsers.size();
        return percent(count, newUserCount);
    }

    /**
     * 지표 2: 월간 1회 이상 루틴 완료 사용자 비율 (%)
     */
    private BigDecimal calculateMonthlyRoutineActiveUserRate(
        LocalDate start,
        LocalDate end,
        LocalDateTime monthEndDt) {
        long totalUsers = userRepository.countByCreatedAtBefore(monthEndDt.plusSeconds(1));
        if (totalUsers == 0) {
            return BigDecimal.ZERO;
        }
        long count = kpiQueryRepository.countDistinctUsersWithRoutineCompletionInPeriod(start, end);
        return percent(count, totalUsers);
    }

    private long countMonthlyActiveUsers(
        LocalDate start,
        LocalDate end,
        LocalDateTime monthStartDt,
        LocalDateTime monthEndDt) {
        return kpiQueryRepository.findDistinctActiveUserIdsInPeriod(start, end, monthStartDt, monthEndDt).size();
    }

    /**
     * 지표 3: 루틴 1개 이상 등록 비율 (%)
     */
    private BigDecimal calculateRoutineRegistrationRate(
        LocalDate start,
        LocalDate end,
        LocalDateTime monthStartDt,
        LocalDateTime monthEndDt,
        long mauCount) {
        if (mauCount == 0) {
            return BigDecimal.ZERO;
        }
        long count = kpiQueryRepository.countDistinctUsersWithRoutineRegistrationInPeriod(monthStartDt, monthEndDt);
        return percent(count, mauCount);
    }

    /**
     * 지표 4: 나가봐요 루틴 1회 이상 완료 비율 (%)
     */
    private BigDecimal calculateOutingRoutineCompletionRate(LocalDate start, LocalDate end, long mauCount) {
        if (mauCount == 0) {
            return BigDecimal.ZERO;
        }
        long count = kpiQueryRepository.countDistinctUsersWithOutingCompletionInPeriod(start, end);
        return percent(count, mauCount);
    }

    /**
     * 지표 6: 긍정 감정 비율 (%)
     */
    private BigDecimal calculatePositiveEmotionRate(LocalDate start, LocalDate end) {
        long total = emotionMarbleRepository.countByDateBetween(start, end);
        if (total == 0) {
            return BigDecimal.ZERO;
        }
        long positive = emotionMarbleRepository.countByDateBetweenAndEmotionMarbleTypeIn(
            start, end, POSITIVE_EMOTION_TYPES);
        return percent(positive, total);
    }

    private static BigDecimal percent(long numerator, long denominator) {
        if (denominator == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(numerator * 100.0 / denominator)
            .setScale(2, RoundingMode.HALF_UP);
    }
}

