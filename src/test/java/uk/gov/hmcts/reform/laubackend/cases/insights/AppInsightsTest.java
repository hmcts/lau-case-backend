package uk.gov.hmcts.reform.laubackend.cases.insights;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.TelemetryContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.GET_ACTIVITY_REQUEST_EXCEPTION;


class AppInsightsTest {
    private AppInsights classUnderTest;

    @Mock
    private TelemetryClient telemetryClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        TelemetryContext telemetryContext = new TelemetryContext();
        doReturn(telemetryContext).when(telemetryClient).getContext();
        classUnderTest = new AppInsights(telemetryClient);
    }

    @Test
    void trackRequest() {
        try {
            classUnderTest.trackEvent(
                GET_ACTIVITY_REQUEST_EXCEPTION.toString(),
                classUnderTest.trackingMap("exception", "Error")
            );
        } catch (Exception exp) {
            fail("classUnderTest.trackEvent() failed.");
        }
    }

    @Test
    void testTelemetry() {
        TelemetryContext telemetryContext = new TelemetryContext();

        TelemetryClient telemetryClient = mock(TelemetryClient.class);
        when(telemetryClient.getContext()).thenReturn(telemetryContext);

        AppInsights appInsights = new AppInsights(telemetryClient);

        assertThat(appInsights).isInstanceOf(AppInsights.class);
    }
}
