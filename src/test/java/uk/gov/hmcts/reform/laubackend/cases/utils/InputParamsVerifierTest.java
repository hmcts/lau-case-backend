package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsConditions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InputParamsVerifierTest {

    @Test
    public void shouldVerifyUserId() {
        assertDoesNotThrow(() -> verifyRequestParamsConditions(new InputParamsHolder("3748238",
                null,
                null,
                null,
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldNotVerifyUserId() {
        try {
            final InputParamsHolder inputParamsHolder = new InputParamsHolder(randomAlphanumeric(65),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid userId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Unable to verify userId path parameter pattern");
        }
    }

    @Test
    public void shouldVerifyCaseRef() {
        assertDoesNotThrow(() -> verifyRequestParamsConditions(new InputParamsHolder(null,
                randomNumeric(16),
                null,
                null,
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldNotVerifyCaseRef() {
        try {
            final InputParamsHolder inputParamsHolder = new InputParamsHolder(null,
                    randomNumeric(17),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid caseRef");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Unable to verify caseRef path parameter pattern");
        }
    }

    @Test
    public void shouldVerifyTimestamp() {
        assertDoesNotThrow(() -> verifyRequestParamsConditions(new InputParamsHolder(null,
                null,
                null,
                null,
                "2021-06-23T22:20:05",
                null,
                null,
                null)));
    }

    @Test
    public void shouldNotVerifyTimestamp() {
        try {
            final InputParamsHolder inputParamsHolder = new InputParamsHolder(null,
                    null,
                    null,
                    null,
                    "2021-106-23T22:20:05",
                    null,
                    null,
                    null);
            verifyRequestParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid timestamp");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Unable to verify timestamp path parameter pattern");
        }
    }

    @Test
    public void shouldVerifyCaseTypeId() {
        assertDoesNotThrow(() -> verifyRequestParamsConditions(new InputParamsHolder(null,
                null,
                randomNumeric(69),
                null,
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldNotVerifyCaseTypeId() {
        try {
            final InputParamsHolder inputParamsHolder = new InputParamsHolder(null,
                    null,
                    randomNumeric(71),
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid case typeId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Unable to verify caseTypeId path parameter pattern");
        }
    }

    @Test
    public void shouldVerifyCaseJurisdictionId() {
        assertDoesNotThrow(() -> verifyRequestParamsConditions(new InputParamsHolder(null,
                null,
                null,
                randomNumeric(69),
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldNotVerifyCaseJurisdictionId() {
        try {
            final InputParamsHolder inputParamsHolder = new InputParamsHolder(null,
                    null,
                    null,
                    randomNumeric(71),
                    null,
                    null,
                    null,
                    null);
            verifyRequestParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid jurisdiction id");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Unable to verify caseJurisdictionId path parameter pattern");
        }
    }

}
