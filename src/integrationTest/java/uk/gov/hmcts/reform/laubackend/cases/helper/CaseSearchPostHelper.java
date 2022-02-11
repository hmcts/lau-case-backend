package uk.gov.hmcts.reform.laubackend.cases.helper;

import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public final class CaseSearchPostHelper {

    private CaseSearchPostHelper() {
    }

    public static CaseSearchPostRequest getCaseSearchPostRequest() {
        final SearchLog searchLog = new SearchLog("3748230", asList("1615817621013640",
                "1615817621013642",
                "1615817621013600",
                "1615817621013601"), "2021-08-24T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }

    public static CaseSearchPostRequest getCaseSearchPostRequest(final String usingId) {
        final SearchLog searchLog = new SearchLog(usingId, asList("1615817621013640",
                "1615817621013642",
                "1615817621013600",
                "1615817621013601"), "2021-08-24T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithMissingCaseRefs() {
        final SearchLog searchLog = new SearchLog("3748230", emptyList(), "2021-08-25T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithMissingUserId() {
        return new CaseSearchPostRequest(new SearchLog(null,
                asList("1615817621013640",
                        "1615817621013642",
                        "1615817621013600",
                        "1615817621013601"),
                "2021-08-23T22:20:05.023Z"));
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithInvalidCaseRefs() {
        return new CaseSearchPostRequest(new SearchLog("3748230",
                asList("12"),
                "2021-08-26T22:20:05.023Z"));
    }
}
