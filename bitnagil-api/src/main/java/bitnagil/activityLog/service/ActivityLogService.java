package bitnagil.activityLog.service;

import bitnagil.activityLog.dto.response.MonthlyActivityLogResponse;
import bitnagil.badge.service.BadgeService;
import bitnagil.emotionMarble.service.EmotionMarbleService;
import bitnagil.user.domain.User;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 월별 활동일지(해당 월 획득 뱃지 + 일별 감정 구슬)를 조합하는 api 오케스트레이션 서비스입니다.
 * 뱃지(badge)와 감정 구슬(emotionMarble) 두 도메인 서비스를 엮어 하나의 응답으로 구성합니다.
 */
@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final BadgeService badgeService;
    private final EmotionMarbleService emotionMarbleService;

    public MonthlyActivityLogResponse getMonthlyActivityLog(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return MonthlyActivityLogResponse.builder()
            .year(year)
            .month(month)
            .badges(badgeService.getMonthlyBadges(user, yearMonth))
            .emotionMarbles(emotionMarbleService.getMonthlyEmotionMarbles(user, yearMonth))
            .build();
    }
}
