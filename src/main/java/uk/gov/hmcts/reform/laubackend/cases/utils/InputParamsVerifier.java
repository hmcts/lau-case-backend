package uk.gov.hmcts.reform.laubackend.cases.utils;

import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASETYPEID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASETYPEID_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_JURISDICTION_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_JURISDICTION_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.CASE_REF_REGEX;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.TIMESTAMP_GET_REGEX;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.TIMESTAMP_POST_REGEX;

@SuppressWarnings("PMD.TooManyMethods")
public final class InputParamsVerifier {

    private InputParamsVerifier() {
    }

    public static void verifyRequestViewParamsAreNotEmpty(final ViewInputParamsHolder inputParamsHolder)
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

    public static void verifyRequestSearchParamsAreNotEmpty(final SearchInputParamsHolder inputParamsHolder)
        throws InvalidRequestException {
        if (isEmpty(inputParamsHolder.getUserId())
            && isEmpty(inputParamsHolder.getCaseRef())
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

    public static void verifyRequestViewParamsConditions(final ViewInputParamsHolder inputParamsHolder)
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

    public static void verifyRequestParamsConditions(final ViewLog viewLog)
            throws InvalidRequestException {
        verifyUserId(viewLog.getUserId(), USERID_POST_EXCEPTION_MESSAGE);
        verifyCaseRef(viewLog.getCaseRef(), CASEREF_POST_EXCEPTION_MESSAGE);
        verifyCaseTypeId(viewLog.getCaseTypeId(), CASETYPEID_POST_EXCEPTION_MESSAGE);
        verifyCaseJurisdictionId(viewLog.getCaseJurisdictionId(), CASE_JURISDICTION_POST_EXCEPTION_MESSAGE);
        verifyTimestamp(viewLog.getTimestamp(), TIMESTAMP_POST_EXCEPTION_MESSAGE, TIMESTAMP_POST_REGEX);
    }

    public static void verifyRequestParamsConditions(final SearchLog searchLog)
            throws InvalidRequestException {

        verifyUserId(searchLog.getUserId(), USERID_POST_EXCEPTION_MESSAGE);
        verifyTimestamp(searchLog.getTimestamp(), TIMESTAMP_POST_EXCEPTION_MESSAGE, TIMESTAMP_POST_REGEX);

        for (String caseRef : searchLog.getCaseRefs()) {
            verifyCaseRef(caseRef, CASEREF_POST_EXCEPTION_MESSAGE);
        }
    }

    private static void verifyTimestamp(final String timestamp,
                                        final String exceptionMessage,
                                        final String regex) throws InvalidRequestException {
        if (!isEmpty(timestamp) && !compile(regex).matcher(timestamp).matches()) {
            throw new InvalidRequestException(exceptionMessage, BAD_REQUEST);
        }
    }

    private static void verifyCaseRef(final String caseRef,
                                      final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(caseRef)
                && !compile(CASE_REF_REGEX).matcher(caseRef).matches()) {
            throw new InvalidRequestException(exceptionMessage, BAD_REQUEST);
        }
    }

    private static void verifyUserId(final String userId,
                                     final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(userId) && userId.length() > 64) {
            throw new InvalidRequestException(exceptionMessage, BAD_REQUEST);
        }
    }

    private static void verifyCaseTypeId(final String caseTypeId,
                                         final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(caseTypeId) && caseTypeId.length() > 70) {
            throw new InvalidRequestException(exceptionMessage, BAD_REQUEST);
        }
    }

    private static void verifyCaseJurisdictionId(final String caseJurisdictionId,
                                                 final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(caseJurisdictionId) && caseJurisdictionId.length() > 70) {
            throw new InvalidRequestException(
                    exceptionMessage,
                    BAD_REQUEST
            );
        }
    }
}
