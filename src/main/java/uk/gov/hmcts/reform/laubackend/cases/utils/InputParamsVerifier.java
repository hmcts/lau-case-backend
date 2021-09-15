package uk.gov.hmcts.reform.laubackend.cases.utils;

import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


public final class InputParamsVerifier {

    private static final String TIMESTAMP_REGEX =
            "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])T(2[0-3]|[01][0-9]):?([0-5][0-9]):?([0-5][0-9])$";
    private static final String CASE_REF_REGEX = "^\\d{16}$";

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
        verifyUserId(inputParamsHolder.getUserId());
        verifyCaseRef(inputParamsHolder.getCaseRef());
        verifyCaseTypeId(inputParamsHolder.getCaseTypeId());
        verifyCaseJurisdictionId(inputParamsHolder.getCaseJurisdictionId());
        verifyTimestamp(inputParamsHolder.getStartTime());
        verifyTimestamp(inputParamsHolder.getEndTime());
    }

    private static void verifyTimestamp(final String timestamp) throws InvalidRequestException {
        if (!isEmpty(timestamp) && !compile(TIMESTAMP_REGEX).matcher(timestamp).matches()) {
            throw new InvalidRequestException("Unable to verify timestamp path parameter pattern", BAD_REQUEST);
        }
    }

    private static void verifyCaseRef(final String caseRef) throws InvalidRequestException {
        if (!isEmpty(caseRef) && !compile(CASE_REF_REGEX).matcher(caseRef).matches()) {
            throw new InvalidRequestException("Unable to verify caseRef path parameter pattern", BAD_REQUEST);
        }
    }

    private static void verifyUserId(final String userId) throws InvalidRequestException {
        if (!isEmpty(userId) && userId.length() > 64) {
            throw new InvalidRequestException("Unable to verify userId path parameter pattern", BAD_REQUEST);
        }
    }

    private static void verifyCaseTypeId(final String caseTypeId) throws InvalidRequestException {
        if (!isEmpty(caseTypeId) && caseTypeId.length() > 70) {
            throw new InvalidRequestException("Unable to verify caseTypeId path parameter pattern", BAD_REQUEST);
        }
    }

    private static void verifyCaseJurisdictionId(final String caseJurisdictionId) throws InvalidRequestException {
        if (!isEmpty(caseJurisdictionId) && caseJurisdictionId.length() > 70) {
            throw new InvalidRequestException(
                    "Unable to verify caseJurisdictionId path parameter pattern",
                    BAD_REQUEST
            );
        }
    }
}
