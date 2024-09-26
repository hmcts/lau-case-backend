package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputParamsVerifierTest {

    @Test
    void verifyAccessRequestGetTimestampDoesNotThrowWithRightTimestamp() {
        assertDoesNotThrow(() -> InputParamsVerifier.verifyAccessRequestGetTimestamp("2023-10-12T12:12:12.000"));
    }

    @Test
    void verifyAccessRequestGetTimestampThrowsWithWrongTimestamp() {
        assertThrows(
            InvalidRequestException.class,
            () -> InputParamsVerifier.verifyAccessRequestGetTimestamp("2021-01-01 00:00:00"));
        assertThrows(
            InvalidRequestException.class,
            () -> InputParamsVerifier.verifyAccessRequestGetTimestamp("2021-01-01T00:00:00Z"));
    }
}
