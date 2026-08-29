package bitnagil.kpi.scheduler;

import bitnagil.kpi.service.KpiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * 매월 1일 전월 성과 KPI를 집계하는 스케줄러입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KpiScheduler {

    private final KpiService kpiService;

    /**
     * 매월 1일 00:10에 실행되어 전월 KPI를 계산·저장합니다.
     */
    @Scheduled(cron = "0 10 0 1 * ?")
    public void calculatePreviousMonthKpi() {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        log.info("KPI 스케줄러 시작: 전월 {} 집계", previousMonth);

        try {
            kpiService.calculateAndSave(previousMonth);
            log.info("KPI 스케줄러 완료: 전월 {} 집계 저장됨", previousMonth);
        } catch (Exception e) {
            log.error("KPI 스케줄러 실패: 전월 {} 집계 중 오류", previousMonth, e);
        }
    }
}
