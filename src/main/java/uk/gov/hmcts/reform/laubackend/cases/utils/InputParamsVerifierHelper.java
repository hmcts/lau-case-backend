package uk.gov.hmcts.reform.laubackend.cases.utils;

import uk.gov.hmcts.reform.laubackend.cases.constants.CaseAction;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.EnumUtils.isValidEnum;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.CASE_REF_REGEX;

public final class InputParamsVerifierHelper {

    private InputParamsVerifierHelper() {
    }

    public static void verifyAction(final String action,
                                    final String caseActionPostExceptionMessage) throws InvalidRequestException {
        if (!isEmpty(action) && !isValidEnum(CaseAction.class, action)) {
            throw new InvalidRequestException(caseActionPostExceptionMessage, BAD_REQUEST);
        }
    }

    public static void verifyTimestamp(final String timestamp,
                                       final String exceptionMessage,
                                       final String regex) throws InvalidRequestException {
        if (!isEmpty(timestamp) && !compile(regex).matcher(timestamp).matches()) {
            throw new InvalidRequestException(exceptionMessage, BAD_REQUEST);
        }
    }

    public static void verifyCaseRef(final String caseRef,
                                     final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(caseRef)
                && !compile(CASE_REF_REGEX).matcher(caseRef).matches()) {
            throw new InvalidRequestException(exceptionMessage, BAD_REQUEST);
        }
    }

    public static void verifyUserId(final String userId,
                                    final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(userId) && userId.length() > 64) {
            throw new InvalidRequestException(exceptionMessage, BAD_REQUEST);
        }
    }

    public static void verifyCaseTypeId(final String caseTypeId,
                                        final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(caseTypeId) && caseTypeId.length() > 70) {
            throw new InvalidRequestException(exceptionMessage, BAD_REQUEST);
        }
    }

    public static void verifyCaseJurisdictionId(final String caseJurisdictionId,
                                                final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(caseJurisdictionId) && caseJurisdictionId.length() > 70) {
            throw new InvalidRequestException(
                    exceptionMessage,
                    BAD_REQUEST
            );
        }
    }
}
