package bitnagil.badge.domain.enums;

import bitnagil.enums.EnumType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 활동 뱃지 종류입니다. 새 뱃지를 추가할 때는 이 enum에 상수만 추가하면 되고,
 * 발급 로직({@code BadgeService})은 수정할 필요가 없습니다.
 */
@RequiredArgsConstructor
@Getter
public enum BadgeType implements EnumType {
    MOTIVATION_EXPERT(
        "의욕 전문가",
        "감정에 솔직해지고 계시네요",
        BadgeTriggerAction.EMOTION_MARBLE_SELECT,
        3,
        "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/badge_motivation_expert.png"),
    CHECK_EXPERT(
        "체크 전문가",
        "큰 성취를 이루셨군요!",
        BadgeTriggerAction.ROUTINE_COMPLETE,
        1,
        "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/badge_check_expert.png"),
    OUTING_EXPERT(
        "외출 전문가",
        "덕분에 도시가 개선되고 있어요!",
        BadgeTriggerAction.REPORT_REGISTER,
        1,
        "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/badge_outing_expert.png"),
    RESERVE_EXPERT(
        "예비 전문가",
        "오늘, 작은 변화를 만들어볼까요?",
        null,
        0,
        "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/badge_reserve_expert.png"),
    ;

    private final String title;
    private final String description;
    private final BadgeTriggerAction triggerAction;
    private final int threshold;
    private final String imageUrl;

    /**
     * 주어진 행동으로 발급 대상이 되는 뱃지 목록을 반환합니다. (RESERVE_EXPERT처럼 트리거가 없는 뱃지는 제외)
     */
    public static List<BadgeType> grantableByAction(BadgeTriggerAction action) {
        return Arrays.stream(values())
            .filter(type -> type.triggerAction == action)
            .toList();
    }
}
