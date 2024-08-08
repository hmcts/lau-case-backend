package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
public final class TestConstants {

    public static final String S2S_NAME = "lau_frontend";
    public static final String S2S_URL = "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal/testing-support";

    // Authorization constants
    public static final String GRANT_TYPE = "password";
    public static final String REDIRECT_URI = "https://lau-case.aat.platform.hmcts.net/oauth2/callback";
    public static final String SCOPE = "openid profile roles";
    public static final String CLIENT_ID = "lau";
    public static final String TOKEN_URL = "https://idam-api.aat.platform.hmcts.net/o/token";


    /*CaseView endPoint*/
    public static final String AUDIT_CASE_ACTION_ENDPOINT = "/audit/caseAction";
    public static final String AUDIT_CASE_ACTION_DELETE_ENDPOINT = "/audit/caseAction/deleteCaseActionRecord";

    public static final String SUCCESS = "Success";

    /*CaseSearch endPoint*/
    public static final String AUDIT_CASE_SEARCH_ENDPOINT = "/audit/caseSearch";
    public static final String AUDIT_CASE_SEARCH_DELETE_ENDPOINT = "/audit/caseSearch/deleteCaseSearchRecord";

    // jurisdictions and case types GET endpoint
    public static final String JURISDICTIONS_CASE_TYPES_ENDPOINT = "/audit/jurisdictionsCaseTypes";

    private TestConstants() {

    }
}
