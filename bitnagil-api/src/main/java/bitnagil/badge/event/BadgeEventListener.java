package bitnagil.badge.event;

import bitnagil.badge.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BadgeEventListener {

    private final BadgeService badgeService;

    @Async("badgeAsyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBadgeTrigger(BadgeTriggerEvent event) {
        try {
            badgeService.evaluateAndGrant(event.userId(), event.action(), event.month());
        } catch (DataIntegrityViolationException e) {
            // 동시 트리거로 인한 중복 수여 시도 - unique 제약이 막아준 정상 케이스이므로 무시
            log.debug("뱃지 동시 수여 레이스 감지(무시) userId={}, action={}, month={}",
                event.userId(), event.action(), event.month());
        } catch (Exception e) {
            log.error("뱃지 발급 실패 userId={}, action={}, month={}",
                event.userId(), event.action(), event.month(), e);
        }
    }
}
