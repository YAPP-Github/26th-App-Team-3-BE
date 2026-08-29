package bitnagil.badge.domain.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 그 달 획득한 뱃지 "개수"에 대응하는 집계 칭호입니다.
 * (0개=예비 전문가, 1개=그 뱃지 자체는 각각 {@link BadgeType#RESERVE_EXPERT}·해당 BadgeType이 대표하므로 여기 미포함.)
 *
 * ⚠️ 현재 기획상 발급 가능한 뱃지가 3종이라 2·3개만 정의돼 있습니다. 문구("하나만 더 모으면 완성")도 "총 3개"를 전제합니다.
 * 발급 가능한 {@link BadgeType}이 늘어 한 달 획득 개수가 4개 이상 가능해지면, 해당 개수의 칭호를 여기 추가해야 합니다.
 * ({@link #from(int)}가 미정의 개수에 fail-fast 하므로, 확장 시 이 enum을 함께 손봐야 함이 드러납니다.)
 */
@RequiredArgsConstructor
@Getter
public enum BadgeTier {
    TWO(2, "능숙한 전문가", "하나만 더 모으면\n이번 달이 완성돼요!"),
    THREE(3, "완벽한 전문가", "꾸준함이 이번 달을\n가득 채웠어요!"),
    ;

    private final int badgeCount;
    private final String title;
    private final String description;

    public static BadgeTier from(int badgeCount) {
        return Arrays.stream(values())
            .filter(tier -> tier.badgeCount == badgeCount)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "뱃지 " + badgeCount + "개에 대한 집계 칭호가 없습니다. 발급 가능한 뱃지가 늘었다면 BadgeTier에 해당 개수 칭호를 추가하세요."));
    }
}
