package uk.gov.hmcts.reform.laubackend.cases.utils;

import uk.gov.hmcts.reform.laubackend.cases.dto.InputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

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

    public static void verifyRequestParamsConditions(final InputParamsHolder inputParamsHolder)
            throws InvalidRequestException {
        verifyUserId(inputParamsHolder.getUserId(), USERID_GET_EXCEPTION_MESSAGE);
        verifyCaseRef(inputParamsHolder.getCaseRef(), CASEREF_GET_EXCEPTION_MESSAGE);
        verifyCaseTypeId(inputParamsHolder.getCaseTypeId(), CASETYPEID_GET_EXCEPTION_MESSAGE);
        verifyCaseJurisdictionId(inputParamsHolder.getCaseJurisdictionId(), CASE_JURISDICTION_GET_EXCEPTION_MESSAGE);
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
