package bitnagil.badge.service;

import bitnagil.badge.domain.Badge;
import bitnagil.badge.domain.enums.BadgeTriggerAction;
import bitnagil.badge.dto.response.BadgeResponse;
import bitnagil.badge.repository.BadgeRepository;
import bitnagil.user.domain.User;
import bitnagil.user.repository.UserRepository;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * 활동 뱃지 발급/조회 로직을 담당하는 서비스입니다.
 * 뱃지는 매월 초기화되며, 수여 판정은 항상 "해당 달의 영속 카운트 기준 재계산"(멱등)이므로
 * async 이벤트 리스너뿐 아니라 뱃지 조회 시점에도 (조회한 달 기준으로) 재평가를 돌려
 * 이벤트가 유실되더라도 그 달의 일지를 열면 정확한 목록이 보이게 합니다. (과거 달 열람 시 소급 발급)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final BadgeMapper badgeMapper;
    private final BadgeGranter badgeGranter;
    private final UserRepository userRepository;

    // 단일 행동(action)에 대해 해당 달(month) 기준으로 재평가 (이벤트 리스너에서 호출)
    public void evaluateAndGrant(Long userId, BadgeTriggerAction action, YearMonth month) {
        userRepository.findById(userId)
            .ifPresent(user -> badgeGranter.evaluateAndGrant(user, action, month));
    }

    /**
     * 해당 연·월에 획득한 활동 뱃지 목록을 조회합니다. 조회 전 조회한 달을 기준으로 전체 행동을 재평가해
     * async 이벤트 유실분을 치유(과거 달이면 소급 발급)합니다.
     * 그 달에 획득한 뱃지가 하나도 없으면 "예비 전문가" 기본 뱃지 1건을 반환합니다(달마다 리셋).
     */
    public List<BadgeResponse> getMonthlyBadges(User user, YearMonth yearMonth) {
        heal(user, yearMonth);

        List<Badge> monthlyBadges = badgeRepository.findByUserAndBadgeYearMonthOrderByCreatedAtDesc(user, yearMonth);
        if (monthlyBadges.isEmpty()) {
            return List.of(badgeMapper.toReserveDefaultResponse());
        }
        return monthlyBadges.stream().map(badgeMapper::toBadgeResponse).toList();
    }

    /**
     * async 이벤트 유실에 대비해 조회 시점에 조회한 달(yearMonth)의 전 액션을 영속 카운트 기준으로 재평가(멱등)해
     * 누락분을 발급합니다. 과거 달을 열람하면 그 달 카운트로 소급 발급됩니다.
     * 치유는 액션별 독립 트랜잭션(BadgeGranter)이라 동시 조회 레이스로 unique 제약 위반이
     * 발생해도(정상 케이스) 조회 응답에는 영향을 주지 않습니다.
     */
    private void heal(User user, YearMonth yearMonth) {
        for (BadgeTriggerAction action : BadgeTriggerAction.values()) {
            try {
                badgeGranter.evaluateAndGrant(user, action, yearMonth);
            } catch (DataIntegrityViolationException e) {
                log.debug("뱃지 동시 수여 레이스 감지(무시) userId={}, action={}, month={}",
                    user.getUserId(), action, yearMonth);
            } catch (Exception e) {
                // 치유는 부가 동작이므로 실패해도 조회는 계속한다.
                log.error("뱃지 조회 시점 재평가 실패 userId={}, action={}, month={}",
                    user.getUserId(), action, yearMonth, e);
            }
        }
    }
}
