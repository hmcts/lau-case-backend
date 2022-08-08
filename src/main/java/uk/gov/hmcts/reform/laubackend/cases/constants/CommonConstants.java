package uk.gov.hmcts.reform.laubackend.cases.constants;

public final class CommonConstants {
    public static final String SERVICE_AUTHORISATION_HEADER = "ServiceAuthorization";
    public static final String AUTHORISATION_HEADER = "Authorization";

    public static final long PERF_TOLERANCE_THRESHOLD_MS = 1500;
    public static final String PERF_THRESHOLD_MESSAGE_BELOW =
        "Good: below threshold (" + PERF_TOLERANCE_THRESHOLD_MS + "ms)";
    public static final String PERF_THRESHOLD_MESSAGE_ABOVE =
        "Bad: above threshold (" + PERF_TOLERANCE_THRESHOLD_MS + "ms)";

    private CommonConstants() {
    }
}
