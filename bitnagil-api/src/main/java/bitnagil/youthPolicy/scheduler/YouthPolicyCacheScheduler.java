package bitnagil.youthPolicy.scheduler;

import bitnagil.youthPolicy.service.YouthPolicyCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 매일 새벽 온통청년 전체 공고 캐시를 갱신하는 스케줄러입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class YouthPolicyCacheScheduler {

    private final YouthPolicyCacheService youthPolicyCacheService;

    /**
     * 매일 04:30에 실행되어 전체 공고 캐시를 갱신합니다(@CachePut 덮어쓰기).
     * 실패해도 예외를 삼켜 기존 캐시가 유지되도록 합니다.
     */
    @Scheduled(cron = "0 30 4 * * ?")
    public void refreshYouthPolicyCache() {
        log.info("청년 공고 캐시 갱신 스케줄러 시작");
        try {
            int count = youthPolicyCacheService.refresh().size();
            log.info("청년 공고 캐시 갱신 완료: {}건", count);
        } catch (Exception e) {
            log.error("청년 공고 캐시 갱신 실패 (기존 캐시 유지)", e);
        }
    }
}
