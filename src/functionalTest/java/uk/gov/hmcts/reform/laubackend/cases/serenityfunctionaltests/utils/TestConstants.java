package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils;

public final class TestConstants {

    public static final String S2S_NAME = "lau_case_frontend";
    public static final String S2S_URL = "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal/testing-support";

    // Authorization constants
    public static final String GRANT_TYPE = "password";
    public static final String USERNAME = "lautest@test.com";
    public static final String PASSWORD = "Password12";
    public static final String REDIRECT_URI = "https://lau-case.aat.platform.hmcts.net/oauth2/callback";
    public static final String SCOPE = "openid profile roles";
    public static final String CLIENT_ID = "lau";
    public static final String CLIENT_SECRET = "laulaulau";
    public static final String TOKEN_URL = "https://idam-api.aat.platform.hmcts.net/o/token";


    /*CaseViw endPoint*/
    public static final String AUDIT_CASE_ACTION_ENDPOINT = "/audit/caseAction";
    public static final String SUCCESS = "Success";

    /*CaseSearch endPoint*/
    public static final String AUDIT_CASE_SEARCH_ENDPOINT = "/audit/caseSearch";

    private TestConstants() {

    }
}
