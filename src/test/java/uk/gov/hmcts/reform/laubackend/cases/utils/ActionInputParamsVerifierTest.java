package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASETYPEID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_JURISDICTION_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestActionParamsConditions;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActionInputParamsVerifierTest {

    @Test
    public void shouldVerifyUserId() {
        assertDoesNotThrow(() -> verifyRequestActionParamsConditions(new ActionInputParamsHolder("3748238",
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
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(randomAlphanumeric(65),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestActionParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid userId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(USERID_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldVerifyCaseRef() {
        assertDoesNotThrow(() -> verifyRequestActionParamsConditions(new ActionInputParamsHolder(null,
                random(16, "123456"),
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
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    random(17, "123456"),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestActionParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid caseRef");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASEREF_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldVerifyTimestamp() {
        assertDoesNotThrow(() -> verifyRequestActionParamsConditions(new ActionInputParamsHolder(null,
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
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    null,
                    null,
                    null,
                    "2021-106-23T22:20:05",
                    null,
                    null,
                    null);
            verifyRequestActionParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid timestamp");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(TIMESTAMP_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldVerifyCaseTypeId() {
        assertDoesNotThrow(() -> verifyRequestActionParamsConditions(new ActionInputParamsHolder(null,
                null,
                random(69, "123456"),
                null,
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldNotVerifyCaseTypeId() {
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    null,
                    random(71, "123456"),
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestActionParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid case typeId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASETYPEID_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldVerifyCaseJurisdictionId() {
        assertDoesNotThrow(() -> verifyRequestActionParamsConditions(new ActionInputParamsHolder(null,
                null,
                null,
                random(69, "123456"),
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldNotVerifyCaseJurisdictionId() {
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    null,
                    null,
                    random(71, "123456"),
                    null,
                    null,
                    null,
                    null);
            verifyRequestActionParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid jurisdiction id");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASE_JURISDICTION_GET_EXCEPTION_MESSAGE);
        }
    }
}
