package bitnagil.emotionMarble.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmotionMarbleType implements EnumType {
    CALM("평온함", 5L,
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_calm.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_calm_v2.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/marble_calm.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/activity_log_calm.png",
            "오늘은 평온하군요~"),
    VITALITY("활기참", 6L,
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_vitality.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_vitality_v2.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/marble_vitality.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/activity_log_vitality.png",
            "오늘은 활기차군요~"),
    LETHARGY("무기력함", 7L,
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_lethargy.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_lethargy_v2.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/marble_lethargy.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/activity_log_lethargy.png",
            "오늘은 무기력한가요?"),
    ANXIETY("불안함", 8L,
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_anxiety.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_anxiety_v2.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/marble_anxiety.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/activity_log_anxiety.png",
            "오늘은 불안한가요?"),
    SATISFACTION("만족함", 9L,
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_satisfaction.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_satisfaction_v2.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/marble_satisfaction.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/activity_log_satisfaction.png",
            "오늘은 만족하는군요~"),
    FATIGUE("피로함", 10L,
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_fatigue.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/home_fatigue_v2.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/marble_fatigue.png",
            "https://bitnagil-s3.s3.ap-northeast-2.amazonaws.com/activity_log_fatigue.png",
            "오늘은 피곤한가요?"),
    ;

    private final String description;
    private final Long caseId;
    private final String homeMarbleImageUrlV1;
    private final String homeMarbleImageUrlV2;
    private final String marbleImageUrl;
    // 활동일지(월별 감정구슬 조회)에서 사용하는 이미지. 구슬 선택 화면(marbleImageUrl)과 시안이 달라 별도로 둔다.
    private final String activityLogImageUrl;
    private final String homeMessage;
}
