package uk.gov.hmcts.reform.laubackend.cases.helper;

import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.util.Arrays.asList;

public final class CaseSearchGetHelper {

    private CaseSearchGetHelper() {
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithUserId(final String userId) {
        final SearchLog searchLog = new SearchLog(userId, asList("1615817621013640",
                "1615817621013642",
                "1615817621013600",
                "1615817621013601"), "2021-08-23T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithCaseRef(final String caseRef) {
        final SearchLog searchLog = new SearchLog("3748230", asList("1615817621013640",
                caseRef,
                "1615817621013600",
                "1615817621013601"), "2021-08-23T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }
}
