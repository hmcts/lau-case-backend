package uk.gov.hmcts.reform.laubackend.cases.utils;

import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public final class NotEmptyInputParamsVerifier {

    private NotEmptyInputParamsVerifier() {
    }

    public static void verifyRequestActionParamsAreNotEmpty(final ActionInputParamsHolder inputParamsHolder)
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

    public static void verifyRequestActionParamsAreNotEmpty(final ActionLog actionLog)
            throws InvalidRequestException {
        if (isEmpty(actionLog.getUserId())
                || isEmpty(actionLog.getCaseAction())
                || isEmpty(actionLog.getCaseRef())
                || isEmpty(actionLog.getCaseTypeId())
                || isEmpty(actionLog.getTimestamp())) {
            throw new InvalidRequestException("You need to populate all required parameters - "
                    + "userId: ".concat(valueOf(actionLog.getUserId())).concat(", ")
                    + "action: ".concat(valueOf(actionLog.getCaseAction())).concat(", ")
                    + "caseRef: ".concat(valueOf(actionLog.getCaseRef())).concat(", ")
                    + "caseTypeId: ".concat(valueOf(actionLog.getCaseTypeId())).concat(", ")
                    + "timestamp: ".concat(valueOf(actionLog.getTimestamp())), BAD_REQUEST);
        }
    }

    public static void verifyRequestSearchParamsAreNotEmpty(final SearchInputParamsHolder inputParamsHolder)
            throws InvalidRequestException {
        if (isEmpty(inputParamsHolder.getUserId())
                && isEmpty(inputParamsHolder.getCaseRef())
                && isEmpty(inputParamsHolder.getStartTime())
                && isEmpty(inputParamsHolder.getEndTime())) {
            throw new InvalidRequestException("At least one path parameter must be present", BAD_REQUEST);
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

