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
    void verifyAccessRequestThrowsForInvalidChallengedRequest() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.CHALLENGED);
        accessRequestLog.setAction(AccessRequestAction.CREATED);

        assertThrows(InvalidRequestException.class, ()
            -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }


    @Test
    void verifyAccessRequestDoesNotThrowForNonSpecificRequestWithNoReason() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.SPECIFIC);
        accessRequestLog.setAction(AccessRequestAction.CREATED);

        assertThrows(InvalidRequestException.class, () ->
            InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

    @Test
    void verifyAccessRequestThrowsForInvalidChallengedRequestWithNoReason() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.CHALLENGED);
        accessRequestLog.setAction(AccessRequestAction.AUTO_APPROVED);

        assertThrows(InvalidRequestException.class, ()
            -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

    @Test
    void verifyAccessRequestDoesNotThrowForSpecificReason() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.SPECIFIC);
        accessRequestLog.setAction(AccessRequestAction.CREATED);
        accessRequestLog.setReason("Super important reason");

        assertDoesNotThrow(() -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

    @Test
    void verifyAccessRequestThrowsForChallengedRequestWithoutEnd() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.CHALLENGED);
        accessRequestLog.setAction(AccessRequestAction.AUTO_APPROVED);
        accessRequestLog.setReason("Some reason");

        assertThrows(InvalidRequestException.class, ()
            -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

    @Test
    void verifyAccessRequestThrowForCSpecificRequestWithEnd() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.SPECIFIC);
        accessRequestLog.setAction(AccessRequestAction.CREATED);
        accessRequestLog.setReason("Some reason");
        accessRequestLog.setRequestEnd("2023-10-12T12:12:12.000");

        assertThrows(InvalidRequestException.class, () ->
            InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

    @Test
    void verifyAccessRequestDoesNotThrowForSpecificRequestWithEnd() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.SPECIFIC);
        accessRequestLog.setAction(AccessRequestAction.APPROVED);
        accessRequestLog.setReason("Some reason");
        accessRequestLog.setRequestEnd("2023-10-12T12:12:12.000");

        assertDoesNotThrow(() -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }

    @Test
    void verifyAccessRequestDoesNotThrowForChallengedRequestWithEnd() {
        AccessRequestLog accessRequestLog = new AccessRequestLog();
        accessRequestLog.setRequestType(AccessRequestType.CHALLENGED);
        accessRequestLog.setAction(AccessRequestAction.AUTO_APPROVED);
        accessRequestLog.setReason("Some reason");
        accessRequestLog.setRequestEnd("2023-10-12T12:12:12.000");

        assertDoesNotThrow(() -> InputParamsVerifier.verifyChallengedAccessRequest(accessRequestLog));
    }


}
