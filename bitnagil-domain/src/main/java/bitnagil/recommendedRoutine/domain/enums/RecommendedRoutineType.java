package bitnagil.recommendedRoutine.domain.enums;

import bitnagil.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RecommendedRoutineType implements EnumType {

    PERSONALIZED("맞춤 추천"),
    OUTING("나가봐요"),
    OUTING_REPORT("나가봐요-제보"),
    GROW("성장해요"),
    REST("쉬어가요"),
    CONNECT("연결해요"),
    WAKE_UP("일어나요");

    private final String description;
}
