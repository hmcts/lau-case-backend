package uk.gov.hmcts.reform.laubackend.cases.utils;

import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SuppressWarnings("PMD.UselessParentheses")
public final class NotEmptyInputParamsVerifier {

    private NotEmptyInputParamsVerifier() {
    }

    public static void verifyRequestActionParamsAreNotEmpty(final ActionInputParamsHolder inputParamsHolder)
            throws InvalidRequestException {
        final boolean isUserIdNotEmpty = !isEmpty(inputParamsHolder.getUserId());
        final boolean isCaseRefNotEmpty = !isEmpty(inputParamsHolder.getCaseRef());
        final boolean isCaseTypeIdNotEmpty = !isEmpty(inputParamsHolder.getCaseTypeId());
        final boolean isCaseJurisdictionIdNotEmpty = !isEmpty(inputParamsHolder.getCaseJurisdictionId());
        final boolean isCaseActionNotEmpty = !isEmpty(inputParamsHolder.getCaseAction());
        final boolean isStartTimeNotEmpty = !isEmpty(inputParamsHolder.getStartTime());
        final boolean isEndTimeNotEmpty = !isEmpty(inputParamsHolder.getEndTime());

        if (!((isStartTimeNotEmpty && isEndTimeNotEmpty)
                && (isUserIdNotEmpty || isCaseRefNotEmpty || isCaseTypeIdNotEmpty || isCaseJurisdictionIdNotEmpty
                || isCaseActionNotEmpty))) {
            throw new InvalidRequestException("Both startTime and endTime must be present "
                    + "and at least one of the parameters (userId, caseRef, caseTypeId, caseJurisdictionId, "
                   + "caseAction) must not be empty", BAD_REQUEST);
        }
    }

    public static void verifyRequestActionParamsAreNotEmpty(final ActionLog actionLog)
            throws InvalidRequestException {
        if (isEmpty(actionLog.getUserId())
                || isEmpty(actionLog.getCaseAction())
                || isEmpty(actionLog.getTimestamp())) {
            throw new InvalidRequestException("You need to populate all required parameters - "
                    + "userId: ".concat(valueOf(actionLog.getUserId())).concat(", ")
                    + "action: ".concat(valueOf(actionLog.getCaseAction())).concat(", ")
                    + "timestamp: ".concat(valueOf(actionLog.getTimestamp())), BAD_REQUEST);
        }
    }

    public static void verifyRequestSearchParamsAreNotEmpty(final SearchInputParamsHolder inputParamsHolder)
            throws InvalidRequestException {
        final boolean isUserIdNotEmpty = !isEmpty(inputParamsHolder.getUserId());
        final boolean isCaseRefNotEmpty = !isEmpty(inputParamsHolder.getCaseRef());
        final boolean isStartTimeNotEmpty = !isEmpty(inputParamsHolder.getStartTime());
        final boolean isEndTimeNotEmpty = !isEmpty(inputParamsHolder.getEndTime());

        if (!((isStartTimeNotEmpty && isEndTimeNotEmpty) && (isUserIdNotEmpty || isCaseRefNotEmpty))) {
            throw new InvalidRequestException("Both startTime and endTime must be present "
                    + "and at least one of the parameters (userId, caseRef) must not be empty", BAD_REQUEST);
        }
    }

    public static void verifyRequestSearchParamsAreNotEmpty(final CaseSearchPostRequest caseSearchPostRequest)
            throws InvalidRequestException {
        if (isEmpty(caseSearchPostRequest.getSearchLog().getUserId())
                || isEmpty(caseSearchPostRequest.getSearchLog().getTimestamp())) {
            throw new InvalidRequestException("You need to populate all mandatory parameters - "
                    + "userId: ".concat(valueOf(caseSearchPostRequest.getSearchLog().getUserId())).concat(", ")
                    + "timestamp: ".concat(valueOf(caseSearchPostRequest.getSearchLog().getTimestamp())),
                    BAD_REQUEST);
        }
    }

    public static void verifyIdNotEmpty(final String id)
            throws InvalidRequestException {
        if (isEmpty(id)) {
            throw new InvalidRequestException("Id must be present", BAD_REQUEST);
        }
    }
}

