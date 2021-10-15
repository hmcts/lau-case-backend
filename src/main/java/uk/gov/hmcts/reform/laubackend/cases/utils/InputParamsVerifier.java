package uk.gov.hmcts.reform.laubackend.cases.utils;

import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.InputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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


public final class InputParamsVerifier {

    private InputParamsVerifier() {
    }

    public static void verifyRequestParamsAreNotEmpty(final InputParamsHolder inputParamsHolder)
            throws InvalidRequestException {
        if (isEmpty(inputParamsHolder.getUserId())
                && isEmpty(inputParamsHolder.getCaseRef())
                && isEmpty(inputParamsHolder.getCaseTypeId())
                && isEmpty(inputParamsHolder.getCaseJurisdictionId())
                && isEmpty(inputParamsHolder.getStartTime())
                && isEmpty(inputParamsHolder.getEndTime())) {
            throw new InvalidRequestException("At least one path parameter must be present", BAD_REQUEST);
        }
    }

    public static void verifyRequestParamsAreNotEmpty(final CaseSearchPostRequest caseSearchPostRequest)
            throws InvalidRequestException {
        if (isEmpty(caseSearchPostRequest.getSearchLog().getUserId())
                || caseSearchPostRequest.getSearchLog().getCaseRefs().isEmpty()
                || isEmpty(caseSearchPostRequest.getSearchLog().getTimestamp())) {
            throw new InvalidRequestException("You need to populate all parameters - "
                    + "userId, caseRefs and timestamp", BAD_REQUEST);
        }
    }


    public static void verifyRequestParamsAreNotEmpty(final ActionLog actionLog)
            throws InvalidRequestException {
        if (isEmpty(actionLog.getUserId())
                || isEmpty(actionLog.getCaseAction())
                || isEmpty(actionLog.getCaseRef())
                || isEmpty(actionLog.getCaseJurisdictionId())
                || isEmpty(actionLog.getCaseTypeId())
                || isEmpty(actionLog.getTimestamp())) {
            throw new InvalidRequestException("You need to populate all required parameters - "
                    + "userId, action, caseRef, caseJurisdictionId, caseTypeId and timestamp", BAD_REQUEST);
        }

    }

    public static void verifyRequestParamsConditions(final InputParamsHolder inputParamsHolder)
            throws InvalidRequestException {
        verifyUserId(inputParamsHolder.getUserId(), USERID_GET_EXCEPTION_MESSAGE);
        verifyCaseRef(inputParamsHolder.getCaseRef(), CASEREF_GET_EXCEPTION_MESSAGE);
        verifyCaseTypeId(inputParamsHolder.getCaseTypeId(), CASETYPEID_GET_EXCEPTION_MESSAGE);
        verifyCaseJurisdictionId(inputParamsHolder.getCaseJurisdictionId(), CASE_JURISDICTION_GET_EXCEPTION_MESSAGE);
        verifyTimestamp(inputParamsHolder.getStartTime(), TIMESTAMP_GET_EXCEPTION_MESSAGE, TIMESTAMP_GET_REGEX);
        verifyTimestamp(inputParamsHolder.getEndTime(), TIMESTAMP_GET_EXCEPTION_MESSAGE, TIMESTAMP_GET_REGEX);
    }

    public static void verifyRequestParamsConditions(final ActionLog actionLog)
            throws InvalidRequestException {
        verifyUserId(actionLog.getUserId(), USERID_POST_EXCEPTION_MESSAGE);
        verifyCaseRef(actionLog.getCaseRef(), CASEREF_POST_EXCEPTION_MESSAGE);
        verifyAction(actionLog.getCaseAction(), CASE_ACTION_POST_EXCEPTION_MESSAGE);
        verifyCaseTypeId(actionLog.getCaseTypeId(), CASETYPEID_POST_EXCEPTION_MESSAGE);
        verifyCaseJurisdictionId(actionLog.getCaseJurisdictionId(), CASE_JURISDICTION_POST_EXCEPTION_MESSAGE);
        verifyTimestamp(actionLog.getTimestamp(), TIMESTAMP_POST_EXCEPTION_MESSAGE, TIMESTAMP_POST_REGEX);
    }

    public static void verifyRequestParamsConditions(final SearchLog searchLog)
            throws InvalidRequestException {

        verifyUserId(searchLog.getUserId(), USERID_POST_EXCEPTION_MESSAGE);
        verifyTimestamp(searchLog.getTimestamp(), TIMESTAMP_POST_EXCEPTION_MESSAGE, TIMESTAMP_POST_REGEX);

        for (String caseRef : searchLog.getCaseRefs()) {
            verifyCaseRef(caseRef, CASEREF_POST_EXCEPTION_MESSAGE);
        }
    }
}
