package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.config;

public final class EnvConfig {

    public static final String API_URL = System.getenv("TEST_URL");
    public static final String IDAM_CLIENT_SECRET = System.getenv("IDAM_CLIENT_SECRET");
    public static final String IDAM_USERNAME = System.getenv("IDAM_USERNAME");
    public static final String IDAM_PASSWORD = System.getenv("IDAM_PASSWORD");


    private EnvConfig() {

    }
}
