package uk.gov.hmcts.reform.laubackend.cases.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
@Slf4j
public class AsyncConfiguration implements AsyncConfigurer {

    @Bean(name = "taskExecutor")
    @Primary
    @Override
    public Executor getAsyncExecutor() {
        log.info("Creating Virtual Thread Executor for async tasks");

        // Create an executor that creates a new virtual thread for each task
        return new Executor() {
            @Override
            public void execute(Runnable command) {
                Thread.startVirtualThread(() -> {
                    String threadName = Thread.currentThread().toString();
                    log.debug("Running task in virtual thread: {}", threadName);
                    command.run();
                });
            }
        };
    }

    // Traditional thread pool executor for compatibility/comparison if needed
    @Bean(name = "traditionalTaskExecutor")
    public Executor getThreadPoolTaskExecutor() {
        log.info("Creating Traditional Thread Pool Executor (backup)");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setMaxPoolSize(10);
        executor.setCorePoolSize(5);
        executor.setQueueCapacity(500);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("Case-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error(
            "Uncaught async error in {}.{} with params: {}",
            method.getDeclaringClass().getSimpleName(),
            method.getName(),
            params,
            ex
        );
    }
}
