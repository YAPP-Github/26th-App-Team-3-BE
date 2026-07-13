package bitnagil.activityLog.controller;

import bitnagil.activityLog.controller.spec.ActivityLogSpec;
import bitnagil.badge.dto.response.BadgeResponse;
import bitnagil.badge.service.BadgeService;
import bitnagil.emotionMarble.dto.response.EmotionMarbleDailyResponse;
import bitnagil.emotionMarble.service.EmotionMarbleService;
import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.user.domain.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/activity-logs")
public class ActivityLogController implements ActivityLogSpec {

    private final BadgeService badgeService;
    private final EmotionMarbleService emotionMarbleService;

    @GetMapping("/badges")
    public CustomResponseDto<List<BadgeResponse>> getMonthlyBadges(
        @CurrentUser User user,
        @RequestParam @Min(2000) @Max(9999) int year,
        @RequestParam @Min(1) @Max(12) int month) {

        return CustomResponseDto.from(badgeService.getMonthlyBadges(user, YearMonth.of(year, month)));
    }

    @GetMapping("/emotion-marbles")
    public CustomResponseDto<List<EmotionMarbleDailyResponse>> getMonthlyEmotionMarbles(
        @CurrentUser User user,
        @RequestParam @Min(2000) @Max(9999) int year,
        @RequestParam @Min(1) @Max(12) int month) {

        return CustomResponseDto.from(emotionMarbleService.getMonthlyEmotionMarbles(user, YearMonth.of(year, month)));
    }
}
