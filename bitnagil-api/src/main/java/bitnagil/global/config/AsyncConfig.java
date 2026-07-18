package bitnagil.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }

    // 실행기 빈이 복수(badgeAsyncExecutor 등)가 되어도, AsyncConfigurer로 qualifier 없는 @Async의
    // 기본 실행기를 이 빈으로 명시 고정한다. @Primary(빈 조회 전역 기본값)보다 범위가 좁아
    // 향후 다른 컴포넌트가 Executor를 타입으로 주입받을 때 의도치 않게 이 풀을 받는 것을 막는다.
    @Override
    public Executor getAsyncExecutor() {
        return asyncExecutor();
    }
}
