package bitnagil.emotionMarble.service;

import bitnagil.emotionMarble.domain.EmotionMarble;
import bitnagil.emotionMarble.dto.request.RegisterEmotionMarbleRequest;
import bitnagil.onboarding.domain.Case;
import bitnagil.user.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 감정 구슬 관련 엔티티 생성 책임을 담당하는 클래스입니다.
 */
@Component
public class EmotionMarbleFactory {

    // 당일의 감정 구슬을 생성
    public EmotionMarble createTodayEmotionMarble(User user, RegisterEmotionMarbleRequest request, LocalDate nowDate,
        LocalDateTime nowDateTime, LocalDateTime endDateTime) {

        return EmotionMarble.builder()
            .emotionMarbleType(request.getEmotionMarbleType())
            .date(nowDate)
            .userId(user.getUserId())
            .historyStartDateTime(nowDateTime)
            .historyEndDateTime(endDateTime) // historyEndDateTime은 당일 11시 59분 59초로 설정(하루씩 설정되기 때문. 이러면 매일 감정 갱신이 불필요함)
            .resultCase( // 감정 구슬에 따른 추천 루틴을 찾기 위해 Case 객체를 생성
                Case.builder()
                    .caseId(request.getEmotionMarbleType().getCaseId())
                    .build()
            ).build();
    }
}
