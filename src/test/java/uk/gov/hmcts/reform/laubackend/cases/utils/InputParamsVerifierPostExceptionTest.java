package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASETYPEID_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_ACTION_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_JURISDICTION_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.appendExceptionParameter;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestActionParamsConditions;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestSearchParamsConditions;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InputParamsVerifierPostExceptionTest {

    @Test
    public void shouldNotVerifyUserIdForCaseAction() {
        final String userId = randomAlphanumeric(65);
        try {
            final ActionLog actionLog = new ActionLog(userId,
                    null,
                    null,
                    null,
                    null,
                    null);


            verifyRequestActionParamsConditions(actionLog);

            fail("The method should have thrown InvalidRequestException due to invalid userId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(USERID_POST_EXCEPTION_MESSAGE, userId));
        }
    }

    @Test
    public void shouldNotVerifyCaseRefForCaseAction() {
        final String caseRef = random(17, "123456");
        try {
            final ActionLog actionLog = new ActionLog(null,
                    null,
                    caseRef,
                    null,
                    null,
                    null);

            verifyRequestActionParamsConditions(actionLog);

            fail("The method should have thrown InvalidRequestException due to invalid caseRef");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(CASEREF_POST_EXCEPTION_MESSAGE, caseRef));
        }
    }

    @Test
    public void shouldNotVerifyTimestampForCaseAction() {
        final String timestamp = "2021-106-23T22:20:05";
        try {
            final ActionLog actionLog = new ActionLog(null,
                    null,
                    null,
                    null,
                    null,
                    timestamp);

            verifyRequestActionParamsConditions(actionLog);

            fail("The method should have thrown InvalidRequestException due to invalid timestamp");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(TIMESTAMP_POST_EXCEPTION_MESSAGE, timestamp));
        }
    }

    @Test
    public void shouldNotVerifyCaseTypeIdForCaseAction() {
        final String caseTypeId = random(71, "123456");
        try {
            final ActionLog actionLog = new ActionLog(null,
                    null,
                    null,
                    null,
                    caseTypeId,
                    null);

            verifyRequestActionParamsConditions(actionLog);

            fail("The method should have thrown InvalidRequestException due to invalid case typeId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(CASETYPEID_POST_EXCEPTION_MESSAGE, caseTypeId));
        }
    }

    @Test
    public void shouldNotVerifyCaseJurisdictionIdForCaseAction() {
        final String caseJurisdictionId = random(71, "123456");
        try {
            final ActionLog actionLog = new ActionLog(null,
                    null,
                    null,
                    caseJurisdictionId,
                    null,
                    null);

            verifyRequestActionParamsConditions(actionLog);

            fail("The method should have thrown InvalidRequestException due to invalid jurisdiction id");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(CASE_JURISDICTION_POST_EXCEPTION_MESSAGE, caseJurisdictionId));
        }
    }

    @Test
    public void shouldNotVerifyUserIdForCaseSearch() {
        final String userId = randomAlphanumeric(65);
        try {
            final SearchLog searchLog = new SearchLog(userId, null, null);

            verifyRequestSearchParamsConditions(searchLog);

            fail("The method should have thrown InvalidRequestException due to invalid userId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(USERID_POST_EXCEPTION_MESSAGE, userId));
        }
    }

    @Test
    public void shouldNotVerifyActionForCaseAction() {
        final String caseAction = "B";
        try {
            final ActionLog actionLog = new ActionLog(null,
                    caseAction,
                    null,
                    null,
                    null,
                    null);

            verifyRequestActionParamsConditions(actionLog);

            fail("The method should have thrown InvalidRequestException due to invalid caseAction");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(CASE_ACTION_POST_EXCEPTION_MESSAGE, caseAction));
        }
    }
}
