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
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.appendExceptionParameter;
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
        final String userId = randomAlphanumeric(65);
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(userId,
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
                    .isEqualTo(appendExceptionParameter(USERID_GET_EXCEPTION_MESSAGE, userId));
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
        final String caseRef = random(17, "123456");
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    caseRef,
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
                    .isEqualTo(appendExceptionParameter(CASEREF_GET_EXCEPTION_MESSAGE, caseRef));
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
        final String timestamp = "2021-106-23T22:20:05";
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    null,
                    null,
                    null,
                    timestamp,
                    null,
                    null,
                    null);
            verifyRequestActionParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid timestamp");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(TIMESTAMP_GET_EXCEPTION_MESSAGE, timestamp));
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
        final String caseTypeId = random(71, "123456");
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    null,
                    caseTypeId,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestActionParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid case typeId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(CASETYPEID_GET_EXCEPTION_MESSAGE, caseTypeId));
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
        final String caseJurisdiction = random(71, "123456");
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    null,
                    null,
                    caseJurisdiction,
                    null,
                    null,
                    null,
                    null);
            verifyRequestActionParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid jurisdiction id");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(CASE_JURISDICTION_GET_EXCEPTION_MESSAGE, caseJurisdiction));
        }
    }
}
