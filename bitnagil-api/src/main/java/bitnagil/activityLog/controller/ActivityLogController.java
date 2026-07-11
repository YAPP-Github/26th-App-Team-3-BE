package bitnagil.activityLog.controller;

import bitnagil.activityLog.controller.spec.ActivityLogSpec;
import bitnagil.activityLog.dto.response.MonthlyActivityLogResponse;
import bitnagil.activityLog.service.ActivityLogService;
import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.user.domain.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    private final ActivityLogService activityLogService;

    @GetMapping
    public CustomResponseDto<MonthlyActivityLogResponse> getMonthlyActivityLog(
        @CurrentUser User user,
        @RequestParam @Min(2000) @Max(9999) int year,
        @RequestParam @Min(1) @Max(12) int month) {

        return CustomResponseDto.from(activityLogService.getMonthlyActivityLog(user, year, month));
    }
}
