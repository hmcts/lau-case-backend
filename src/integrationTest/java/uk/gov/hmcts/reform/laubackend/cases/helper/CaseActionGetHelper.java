package uk.gov.hmcts.reform.laubackend.cases.helper;

import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;

@SuppressWarnings({"PMD.UseObjectForClearerAPI"})
public final class CaseActionGetHelper {

    private CaseActionGetHelper() {
    }

    public static CaseActionPostRequest getCaseActionPostRequest(final String userId,
                                                                 final String caseRef,
                                                                 final String caseJurisdictionId,
                                                                 final String caseTypeId,
                                                                 final String caseAction,
                                                                 final String timestamp) {
        final ActionLog actionLog = new ActionLog(userId == null ? "1" : userId,
                caseAction == null ? "CREATE" : caseAction,
                caseRef == null ? "0000000000000000" : caseRef,
                caseJurisdictionId == null ? "2" : caseJurisdictionId,
                caseTypeId == null ? "3" : caseTypeId,
                timestamp == null ? "2021-08-23T22:20:05.023Z" : timestamp);

        return new CaseActionPostRequest(actionLog);
    }
}
