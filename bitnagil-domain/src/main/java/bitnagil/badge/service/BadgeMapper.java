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
        return BadgeResponse.builder()
            .badgeType(badge.getBadgeType())
            .imageUrl(badge.getBadgeType().getImageUrl())
            .acquiredAt(badge.getCreatedAt())
            .build();
    }

    // 획득한 뱃지가 하나도 없을 때 보여줄 기본 뱃지("예비 전문가")
    public BadgeResponse toReserveDefaultResponse() {
        BadgeType type = BadgeType.RESERVE_EXPERT;
        return BadgeResponse.builder()
            .badgeType(type)
            .imageUrl(type.getImageUrl())
            .acquiredAt(null)
            .build();
    }

    /**
     * 그 달 획득 뱃지 목록을 대표 칭호(badgeTitle/badgeDescription) + 아이콘 목록(badges)으로 묶습니다.
     * badges[] 항목은 아이콘 표시용이라 title/description을 담지 않으므로, 대표 칭호는 원본
     * {@link Badge}/{@link BadgeType}에서 직접 계산합니다: 0개=예비 전문가, 1개=그 뱃지 자체,
     * 2개 이상=집계 칭호({@link BadgeTier}).
     */
    public MonthlyBadgeResponse toMonthlyBadgeResponse(List<Badge> monthlyBadges) {
        if (monthlyBadges.isEmpty()) {
            BadgeType reserve = BadgeType.RESERVE_EXPERT;
            return MonthlyBadgeResponse.builder()
                .badgeTitle(reserve.getTitle())
                .badgeDescription(reserve.getDescription())
                .badges(List.of(toReserveDefaultResponse()))
                .build();
        }

        String badgeTitle;
        String badgeDescription;
        if (monthlyBadges.size() == 1) {
            BadgeType type = monthlyBadges.get(0).getBadgeType();
            badgeTitle = type.getTitle();
            badgeDescription = type.getDescription();
        } else {
            BadgeTier tier = BadgeTier.from(monthlyBadges.size());
            badgeTitle = tier.getTitle();
            badgeDescription = tier.getDescription();
        }

        return MonthlyBadgeResponse.builder()
            .badgeTitle(badgeTitle)
            .badgeDescription(badgeDescription)
            .badges(monthlyBadges.stream().map(this::toBadgeResponse).toList())
            .build();
    }
}
