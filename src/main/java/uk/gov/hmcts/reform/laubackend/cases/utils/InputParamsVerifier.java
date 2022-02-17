package uk.gov.hmcts.reform.laubackend.cases.utils;

import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASETYPEID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASETYPEID_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_ACTION_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_JURISDICTION_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_JURISDICTION_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.TIMESTAMP_GET_REGEX;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.TIMESTAMP_POST_REGEX;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifierHelper.verifyAction;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifierHelper.verifyCaseJurisdictionId;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifierHelper.verifyCaseRef;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifierHelper.verifyCaseTypeId;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifierHelper.verifyTimestamp;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifierHelper.verifyUserId;

@SuppressWarnings("PMD.TooManyMethods")
public final class InputParamsVerifier {
    private InputParamsVerifier() {
    }

    public static void verifyRequestActionParamsConditions(final ActionLog actionLog)
            throws InvalidRequestException {
        verifyUserId(actionLog.getUserId(), USERID_POST_EXCEPTION_MESSAGE);
        verifyCaseRef(actionLog.getCaseRef(), CASEREF_POST_EXCEPTION_MESSAGE);
        verifyAction(actionLog.getCaseAction(), CASE_ACTION_POST_EXCEPTION_MESSAGE);
        verifyCaseTypeId(actionLog.getCaseTypeId(), CASETYPEID_POST_EXCEPTION_MESSAGE);
        verifyCaseJurisdictionId(actionLog.getCaseJurisdictionId(), CASE_JURISDICTION_POST_EXCEPTION_MESSAGE);
        verifyTimestamp(actionLog.getTimestamp(), TIMESTAMP_POST_EXCEPTION_MESSAGE, TIMESTAMP_POST_REGEX);
    }

    public static void verifyRequestActionParamsConditions(final ActionInputParamsHolder inputParamsHolder)
            throws InvalidRequestException {
        verifyUserId(inputParamsHolder.getUserId(), USERID_GET_EXCEPTION_MESSAGE);
        verifyCaseRef(inputParamsHolder.getCaseRef(), CASEREF_GET_EXCEPTION_MESSAGE);
        verifyCaseTypeId(inputParamsHolder.getCaseTypeId(), CASETYPEID_GET_EXCEPTION_MESSAGE);
        verifyCaseJurisdictionId(inputParamsHolder.getCaseJurisdictionId(), CASE_JURISDICTION_GET_EXCEPTION_MESSAGE);
        verifyTimestamp(inputParamsHolder.getStartTime(), TIMESTAMP_GET_EXCEPTION_MESSAGE, TIMESTAMP_GET_REGEX);
        verifyTimestamp(inputParamsHolder.getEndTime(), TIMESTAMP_GET_EXCEPTION_MESSAGE, TIMESTAMP_GET_REGEX);
    }

    public static void verifyRequestSearchParamsConditions(final SearchInputParamsHolder inputParamsHolder)
            throws InvalidRequestException {
        verifyUserId(inputParamsHolder.getUserId(), USERID_GET_EXCEPTION_MESSAGE);
        verifyCaseRef(inputParamsHolder.getCaseRef(), CASEREF_GET_EXCEPTION_MESSAGE);
        verifyTimestamp(inputParamsHolder.getStartTime(), TIMESTAMP_GET_EXCEPTION_MESSAGE, TIMESTAMP_GET_REGEX);
        verifyTimestamp(inputParamsHolder.getEndTime(), TIMESTAMP_GET_EXCEPTION_MESSAGE, TIMESTAMP_GET_REGEX);
    }

    public static void verifyRequestSearchParamsConditions(final SearchLog searchLog)
            throws InvalidRequestException {

        verifyUserId(searchLog.getUserId(), USERID_POST_EXCEPTION_MESSAGE);
        verifyTimestamp(searchLog.getTimestamp(), TIMESTAMP_POST_EXCEPTION_MESSAGE, TIMESTAMP_POST_REGEX);
        if (!isEmpty(searchLog.getCaseRefs())) {
            searchLog.getCaseRefs().removeIf(caseRef -> caseRef == null || caseRef.equals(""));
            for (String caseRef : searchLog.getCaseRefs()) {
                verifyCaseRef(caseRef, CASEREF_POST_EXCEPTION_MESSAGE);
            }
        }
    }
}
