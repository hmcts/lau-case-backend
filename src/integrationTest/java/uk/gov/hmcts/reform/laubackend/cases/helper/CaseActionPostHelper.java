package uk.gov.hmcts.reform.laubackend.cases.helper;

import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
public final class CaseActionPostHelper {

    private CaseActionPostHelper() {
    }

    public static CaseActionPostRequest getCaseActionPostRequest() {
        final ActionLog actionLog = new ActionLog("1",
                "CREATE",
                "1615817621013640",
                "3",
                "4",
                "2021-08-23T22:20:05.023Z");

        return new CaseActionPostRequest(actionLog);
    }

    public static CaseActionPostRequest getCaseActionPostRequest(final String userId) {
        final ActionLog actionLog = new ActionLog(userId,
                "CREATE",
                "1615817621013640",
                "3",
                "4",
                "2021-08-23T22:20:05.023Z");

        return new CaseActionPostRequest(actionLog);
    }

    public static CaseActionPostRequest getCaseActionPostRequestWithMissingMandatoryParameter() {
        final ActionLog actionLog = new ActionLog("1",
                null,
                "1615817621013640",
                "3",
                "4",
                "2021-08-23T22:20:05.023Z");

        return new CaseActionPostRequest(actionLog);
    }

    public static CaseActionPostRequest getCaseActionPostRequestWithInvalidParameter() {
        final ActionLog actionLog = new ActionLog("1",
                "C",
                "1615817621013640",
                "3",
                "4",
                "2021-08-23T22:20:05.023Z");

        return new CaseActionPostRequest(actionLog);
    }

}