package bitnagil.badge.service;

import bitnagil.badge.domain.Badge;
import bitnagil.badge.domain.enums.BadgeType;
import bitnagil.badge.dto.response.BadgeResponse;
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
}
