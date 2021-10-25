package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

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
                || isEmpty(actionLog.getCaseJurisdictionId())
                || isEmpty(actionLog.getCaseTypeId())
                || isEmpty(actionLog.getTimestamp())) {
            throw new InvalidRequestException("You need to populate all required parameters - "
                    + "userId, action, caseRef, caseJurisdictionId, caseTypeId "
                    + "and timestamp", BAD_REQUEST);
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
                || isEmpty(caseSearchPostRequest.getSearchLog().getTimestamp())
                || CollectionUtils.isEmpty(caseSearchPostRequest.getSearchLog().getCaseRefs())) {
            throw new InvalidRequestException("You need to populate all parameters - "
                    + "userId, caseRefs and timestamp", BAD_REQUEST);
        }
    }
}

