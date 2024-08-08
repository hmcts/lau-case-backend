package uk.gov.hmcts.reform.laubackend.cases.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseActionAuditRepository;

/**
 * Simple service class to manage jurisdictions and case types cache. Fetching from the
 * database takes more than 30s, that is more than the gateway timeout. Therefore, we
 * need to cache the results. We don't expect this data to change frequently, nor to be a
 * large dataset, therefore keeping in memory is not a problem.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledCacheManager {

    private final CaseActionAuditRepository caseActionAuditRepository;
    private final CacheManager cacheManager;

    /**
     * This method is called after the application has started and also every day at
     * random minute past 01:00AM (Spring calculates this random value at startup, and
     * then it stays that until restart). It clears the cache and re-caches the
     * jurisdictions and case types. Randomness is needed because we have multiple
     * instances running in production and we don't want them all hit the database at the
     * same time. Please note, cron expression is extended java version with seconds as a
     * first parameter.
     */
    @EventListener(ApplicationReadyEvent.class)
    @SuppressWarnings("java:S6857")
    @Scheduled(cron = "0 ${random.int[0,59]} 01 * * *")
    public void cacheData() {
        log.info("Re-caching jurisdictionsCaseTypes");
        Cache cache = cacheManager.getCache("jurisdictionsCaseTypes");
        if (cache != null) {
            cache.clear();
        }
        caseActionAuditRepository.getJurisdictionsCaseTypes();
        log.info("Re-caching done.");
    }
}
