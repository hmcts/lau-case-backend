package uk.gov.hmcts.reform.laubackend.cases.insights;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.TelemetryContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.GET_ACTIVITY_REQUEST_EXCEPTION;


class AppInsightsTest {
    private AppInsights classUnderTest;

    @Mock
    private TelemetryClient telemetryClient;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        TelemetryContext telemetryContext = new TelemetryContext();
        doReturn(telemetryContext).when(telemetryClient).getContext();
        classUnderTest = new AppInsights(telemetryClient);
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @AfterEach
    void close() throws Exception {
        autoCloseable.close();
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
        assertThat(classUnderTest).isInstanceOf(AppInsights.class);
    }
}
