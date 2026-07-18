package bitnagil.badge.service;

import bitnagil.badge.domain.Badge;
import bitnagil.badge.domain.enums.BadgeType;
import bitnagil.badge.domain.enums.BadgeTriggerAction;
import bitnagil.badge.repository.BadgeRepository;
import bitnagil.emotionMarble.repository.EmotionMarbleRepository;
import bitnagil.report.repository.ReportRepository;
import bitnagil.routineV2.repository.RoutineV2Repository;
import bitnagil.user.domain.User;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 뱃지 수여 판정/저장을 담당하는 컴포넌트입니다.
 * 뱃지는 매월 초기화되므로 발급 판정은 "해당 달(month)의 활동 카운트"를 기준으로 합니다.
 * BadgeService에서 분리한 이유: 트랜잭션 경계를 이 메서드로 고정해(액션별 독립 커밋)
 * 동시 수여 레이스로 unique 제약 위반이 나도 호출자가 커밋 예외를 개별 처리할 수 있게 하고,
 * self-invocation 시 @Transactional이 무시되는 프록시 한계를 피한다.
 */
@Component
@RequiredArgsConstructor
public class BadgeGranter {

    private final BadgeRepository badgeRepository;
    private final EmotionMarbleRepository emotionMarbleRepository;
    private final RoutineV2Repository routineV2Repository;
    private final ReportRepository reportRepository;

    /**
     * 행동(action)으로 획득 가능한 뱃지들을 해당 달(month)의 카운트 기준으로 재평가해,
     * 그 달에 미보유 + 임계치 충족 시 수여합니다.
     * 영속된 카운트만으로 재계산하는 멱등 연산이므로 이벤트 리스너와 조회 시점 치유(과거 달 소급 포함) 양쪽에서 재사용합니다.
     */
    // REQUIRES_NEW: 액션별 독립 커밋을 강제한다. 호출자(조회 치유 등)가 트랜잭션 안에 있어도
    // 동시 수여 unique 위반이 호출자 트랜잭션을 rollback-only로 오염시키지 않도록 별도 트랜잭션으로 분리한다.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void evaluateAndGrant(User user, BadgeTriggerAction action, YearMonth month) {
        List<BadgeType> missingTypes = BadgeType.grantableByAction(action).stream()
            .filter(type -> !badgeRepository.existsByUserAndBadgeTypeAndBadgeYearMonth(user, type, month))
            .toList();
        if (missingTypes.isEmpty()) {
            return;
        }

        // countProgress는 action·month에만 의존(type 무관)하므로, 미보유 타입이 있을 때 한 번만 계산해 공유한다.
        long progress = countProgress(user, action, month);
        for (BadgeType type : missingTypes) {
            if (progress >= type.getThreshold()) {
                badgeRepository.save(Badge.grant(user, type, month));
            }
        }
    }

    // 해당 달(month)의 활동 카운트 (월별 리셋)
    private long countProgress(User user, BadgeTriggerAction action, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();
        return switch (action) {
            case EMOTION_MARBLE_SELECT ->
                emotionMarbleRepository.countByUserIdAndDateBetween(user.getUserId(), startDate, endDate);
            case ROUTINE_COMPLETE ->
                routineV2Repository.countCompletedByUserAndRoutineDateBetween(user, startDate, endDate);
            case REPORT_REGISTER ->
                reportRepository.countByUserAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                    user, startDate.atStartOfDay(), month.plusMonths(1).atDay(1).atStartOfDay());
        };
    }
}
