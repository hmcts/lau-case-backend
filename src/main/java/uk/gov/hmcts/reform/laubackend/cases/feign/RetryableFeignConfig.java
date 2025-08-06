package uk.gov.hmcts.reform.laubackend.cases.feign;

import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Configuration
public class RetryableFeignConfig {

    @Value("${delayOnError:3}")
    long initialDelay;
    long maxDelay = 10;
    int maxRetries = 3;

    @Bean
    public Retryer feignRetryer() {
        // Default backoff calculated using the following formula:
        // Math.min(period * Math.pow(1.5, attempt - 1), maxPeriod)
        // First retry will be after 30s, second after 45s, third after 67.5s
        log.info("Feign retryer configured with initial delay: {} seconds, max delay: {} seconds, max retries: {}",
                initialDelay, maxDelay, maxRetries);
        return new Retryer.Default(
            SECONDS.toMillis(initialDelay),
            SECONDS.toMillis(maxDelay),
            maxRetries);
    }
}
