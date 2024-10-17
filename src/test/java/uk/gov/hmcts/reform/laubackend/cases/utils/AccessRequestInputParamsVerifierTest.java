package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestAction;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccessRequestInputParamsVerifierTest {

    @Test
    void verifyAccessRequestGetTimestampDoesNotThrowWithRightTimestamp() {
        assertDoesNotThrow(() -> InputParamsVerifier.verifyAccessRequestGetTimestamp("2023-10-12T12:12:12.000"));
    }

    @Test
    void verifyAccessRequestGetTimestampThrowsWithWrongTimestamp() {
        assertThrows(
            InvalidRequestException.class,
            () -> InputParamsVerifier.verifyAccessRequestGetTimestamp("2021-01-01 00:00:00")
        );
        assertThrows(
            InvalidRequestException.class,
            () -> InputParamsVerifier.verifyAccessRequestGetTimestamp("2021-01-01T00:00:00Z")
        );
    }


    @Test
    void verifyAccessRequestDoesNotThrowForValidChallengedRequest() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.CHALLENGED);
        accessRequestLog.setAction(AccessRequestAction.AUTO_APPROVED);

        assertDoesNotThrow(() -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

    @Test
    void verifyAccessRequestThrowsForInvalidChallengedRequest() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.CHALLENGED);
        accessRequestLog.setAction(AccessRequestAction.CREATED);

        assertThrows(InvalidRequestException.class, ()
            -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }


    @Test
    void verifyAccessRequestDoesNotThrowForNonChallengedRequest() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.SPECIFIC);
        accessRequestLog.setAction(AccessRequestAction.CREATED);

        assertDoesNotThrow(() -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

    @Test
    void verifyAccessRequestThrowsForInvalidSpecificRequest() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.SPECIFIC);
        accessRequestLog.setAction(AccessRequestAction.AUTO_APPROVED);

        assertThrows(InvalidRequestException.class, ()
            -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

}
