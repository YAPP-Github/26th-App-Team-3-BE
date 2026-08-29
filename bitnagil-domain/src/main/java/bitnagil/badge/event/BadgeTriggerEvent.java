package bitnagil.badge.event;

import bitnagil.badge.domain.enums.BadgeTriggerAction;
import java.time.YearMonth;

/**
 * 뱃지 발급 대상이 될 수 있는 사용자 행동이 커밋되었을 때 발행되는 이벤트입니다.
 * async 리스너에서 지연 없이 재조회할 수 있도록 엔티티가 아닌 userId만 담고,
 * 뱃지가 귀속될 달(month)은 행동 시점에 확정해 함께 전달합니다.
 * (비동기 처리가 월 경계를 넘어 지연되더라도 정확한 달에 발급되도록)
 */
public record BadgeTriggerEvent(Long userId, BadgeTriggerAction action, YearMonth month) {
}
