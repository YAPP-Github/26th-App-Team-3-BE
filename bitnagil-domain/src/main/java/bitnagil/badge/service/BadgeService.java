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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 활동 뱃지 발급/조회 로직을 담당하는 서비스입니다.
 * 뱃지는 오직 사용자의 실제 행동(감정구슬 선택·루틴 완료·제보 등록)이 발행하는 이벤트로만 생성됩니다.
 * 조회(열람)는 행동이 아니므로 뱃지 생성에 관여하지 않으며, {@link #getMonthlyBadges}는 순수 조회입니다.
 */
@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final BadgeMapper badgeMapper;
    private final BadgeGranter badgeGranter;
    private final UserRepository userRepository;

    // 단일 행동(action)에 대해 해당 달(month) 기준으로 평가/발급합니다. (이벤트 리스너 전용 — 뱃지 생성의 유일한 경로)
    public void evaluateAndGrant(Long userId, BadgeTriggerAction action, YearMonth month) {
        userRepository.findById(userId)
            .ifPresent(user -> badgeGranter.evaluateAndGrant(user, action, month));
    }

    /**
     * 해당 연·월에 획득한 활동 뱃지 목록을 조회합니다(순수 조회, 발급 로직 없음).
     * 그 달에 획득한 뱃지가 하나도 없으면 "예비 전문가" 기본 뱃지 1건을 반환합니다(달마다 리셋).
     */
    @Transactional(readOnly = true)
    public List<BadgeResponse> getMonthlyBadges(User user, YearMonth yearMonth) {
        List<Badge> monthlyBadges = badgeRepository.findByUserAndBadgeYearMonthOrderByCreatedAtDesc(user, yearMonth);
        if (monthlyBadges.isEmpty()) {
            return List.of(badgeMapper.toReserveDefaultResponse());
        }
        return monthlyBadges.stream().map(badgeMapper::toBadgeResponse).toList();
    }
}
