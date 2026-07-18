package bitnagil.badge.service;

import bitnagil.badge.domain.Badge;
import bitnagil.badge.domain.enums.BadgeTier;
import bitnagil.badge.domain.enums.BadgeType;
import bitnagil.badge.dto.response.BadgeResponse;
import bitnagil.badge.dto.response.MonthlyBadgeResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BadgeMapper {

    public BadgeResponse toBadgeResponse(Badge badge) {
        BadgeType type = badge.getBadgeType();
        return BadgeResponse.builder()
            .badgeType(type)
            .title(type.getTitle())
            .description(type.getDescription())
            .imageUrl(type.getImageUrl())
            .acquiredAt(badge.getCreatedAt())
            .build();
    }

    // 획득한 뱃지가 하나도 없을 때 보여줄 기본 뱃지("예비 전문가")
    public BadgeResponse toReserveDefaultResponse() {
        BadgeType type = BadgeType.RESERVE_EXPERT;
        return BadgeResponse.builder()
            .badgeType(type)
            .title(type.getTitle())
            .description(type.getDescription())
            .imageUrl(type.getImageUrl())
            .acquiredAt(null)
            .build();
    }

    /**
     * 그 달 획득 뱃지 목록을 대표 칭호(title/description) + 아이콘 목록으로 묶습니다.
     * 대표 칭호는 획득 개수로 결정됩니다: 0·1개=대표 뱃지(예비 전문가 또는 그 뱃지) 자체, 2개 이상=집계 칭호({@link BadgeTier}).
     */
    public MonthlyBadgeResponse toMonthlyBadgeResponse(List<Badge> monthlyBadges) {
        List<BadgeResponse> badges = monthlyBadges.isEmpty()
            ? List.of(toReserveDefaultResponse())
            : monthlyBadges.stream().map(this::toBadgeResponse).toList();

        String title;
        String description;
        if (badges.size() >= 2) {
            BadgeTier tier = BadgeTier.from(badges.size());
            title = tier.getTitle();
            description = tier.getDescription();
        } else {
            // 0개(예비 전문가 기본)·1개(그 뱃지) 모두 목록의 대표 뱃지가 곧 칭호가 된다.
            BadgeResponse representative = badges.get(0);
            title = representative.getTitle();
            description = representative.getDescription();
        }

        return MonthlyBadgeResponse.builder()
            .title(title)
            .description(description)
            .badges(badges)
            .build();
    }
}
