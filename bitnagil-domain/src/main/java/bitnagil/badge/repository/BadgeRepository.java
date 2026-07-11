package bitnagil.badge.repository;

import bitnagil.badge.domain.Badge;
import bitnagil.badge.domain.enums.BadgeType;
import bitnagil.user.domain.User;
import java.time.YearMonth;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    // 해당 달(badgeMonth)에 이미 그 종류의 뱃지를 획득했는지 (월별 중복 수여 방지)
    boolean existsByUserAndBadgeTypeAndBadgeMonth(User user, BadgeType badgeType, YearMonth badgeMonth);

    // 해당 달(badgeMonth)에 획득한 뱃지를 최신순으로 조회
    List<Badge> findByUserAndBadgeMonthOrderByCreatedAtDesc(User user, YearMonth badgeMonth);
}
