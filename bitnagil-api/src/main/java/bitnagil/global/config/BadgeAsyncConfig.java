package bitnagil.global.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 뱃지 발급 전용 executor. {@code AsyncConfig}의 공유 executor(큐 100 + 기본 AbortPolicy)를
 * 그대로 쓰면 포화 시 태스크가 드롭될 수 있어, threshold=1인 단발성 뱃지(루틴 완료/제보)가
 * 영구 누락될 위험이 있다. 전용 executor로 격리하고 CallerRunsPolicy로 드롭 대신 백프레셔를 준다.
 * ({@code @EnableAsync}는 {@code AsyncConfig}에 이미 선언되어 있어 여기서는 재선언하지 않는다.)
 */
@Configuration
public class BadgeAsyncConfig {

    @Bean
    public Executor badgeAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(500);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("badge-async-");
        executor.initialize();
        return executor;
    }
}
