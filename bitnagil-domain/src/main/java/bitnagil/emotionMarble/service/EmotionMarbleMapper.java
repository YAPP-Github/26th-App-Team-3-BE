package bitnagil.emotionMarble.service;

import bitnagil.emotionMarble.domain.EmotionMarble;
import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import bitnagil.emotionMarble.dto.response.EmotionMarbleDailyResponse;
import bitnagil.emotionMarble.dto.response.EmotionMarbleTypeResponse;
import bitnagil.emotionMarble.dto.response.EmotionMarbleTypeResponseV2;
import org.springframework.stereotype.Component;

/**
 * 감정구슬에 대한 가공된 데이터를 DTO로 변환하는 Mapper 클래스입니다.
 */
@Component
public class EmotionMarbleMapper {

    public EmotionMarbleTypeResponse toEmotionMarbleTypeResponse(EmotionMarbleType emotionMarbleType) {
        return EmotionMarbleTypeResponse.builder()
                .emotionMarbleName(emotionMarbleType.getDescription())
                .emotionMarbleType(emotionMarbleType)
                .imageUrl(emotionMarbleType.getMarbleImageUrl())
                .build();
    }

    // todo: v2로 전환 시 deprecated 처리
    @Deprecated
    public EmotionMarbleTypeResponse toEmotionMarbleTypeResponse(EmotionMarble emotionMarble) {
        return EmotionMarbleTypeResponse.builder()
            .emotionMarbleType(emotionMarble == null ? null : emotionMarble.getEmotionMarbleType())
            .emotionMarbleName(emotionMarble == null ? null : emotionMarble.getEmotionMarbleType().getDescription())
            .imageUrl(emotionMarble == null ? null :emotionMarble.getEmotionMarbleType().getHomeMarbleImageUrlV1())
            .build();
    }

    public EmotionMarbleTypeResponseV2 toEmotionMarbleTypeResponseV2(EmotionMarble emotionMarble) {
        return EmotionMarbleTypeResponseV2.builder()
                .emotionMarbleType(emotionMarble == null ? null : emotionMarble.getEmotionMarbleType())
                .emotionMarbleName(emotionMarble == null ? null : emotionMarble.getEmotionMarbleType().getDescription())
                .imageUrl(emotionMarble == null ? null :emotionMarble.getEmotionMarbleType().getHomeMarbleImageUrlV2())
                .emotionMarbleHomeMessage(emotionMarble == null ? null : emotionMarble.getEmotionMarbleType().getHomeMessage())
                .build();
    }

    // 월별 조회용 일별 감정 구슬 매핑 (조회 결과 목록의 요소이므로 null이 아님)
    public EmotionMarbleDailyResponse toDailyResponse(EmotionMarble emotionMarble) {
        EmotionMarbleType emotionMarbleType = emotionMarble.getEmotionMarbleType();
        return EmotionMarbleDailyResponse.builder()
            .date(emotionMarble.getDate())
            .emotionMarbleType(emotionMarbleType)
            .emotionMarbleName(emotionMarbleType.getDescription())
            .imageUrl(emotionMarbleType.getMarbleImageUrl())
            .build();
    }
}
