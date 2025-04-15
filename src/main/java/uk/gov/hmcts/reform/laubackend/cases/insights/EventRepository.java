package uk.gov.hmcts.reform.laubackend.cases.insights;

import java.util.Map;

@FunctionalInterface
public interface EventRepository {

    void trackEvent(String name, Map<String, String> properties);
}
