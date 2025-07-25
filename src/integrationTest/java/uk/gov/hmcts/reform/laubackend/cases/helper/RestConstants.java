package uk.gov.hmcts.reform.laubackend.cases.helper;

public final class RestConstants {
    public static final String SERVICE_AUTHORISATION_HEADER = "ServiceAuthorization";
    public static final String AUTHORISATION_HEADER = "Authorization";

    public static final String GOOD_TOKEN = "good_token";
    public static final String BAD_S2S_TOKEN = "bad_token";
    public static final String SERVICE_UNAVAILABLE_S2S_TOKEN = "unavailable_token";
    public static final String INTERNAL_SERVER_S2S_TOKEN = "server_error_token";


    public static final String START_TIME = "2021-07-23T22:20:05";
    public static final String END_TIME = "2021-09-23T22:20:05";

    public static final String START_TIME_PARAMETER = "startTimestamp";
    public static final String END_TIME_PARAMETER = "endTimestamp";

    private RestConstants() {
    }
}
