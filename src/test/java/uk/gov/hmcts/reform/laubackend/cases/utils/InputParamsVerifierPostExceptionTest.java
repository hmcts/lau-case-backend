package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASETYPEID_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_JURISDICTION_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_POST_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsConditions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InputParamsVerifierPostExceptionTest {

    @Test
    public void shouldNotVerifyUserId() {
        try {
            final ViewLog viewLog = new ViewLog();
            viewLog.setUserId(randomAlphanumeric(65));

            verifyRequestParamsConditions(viewLog);

            fail("The method should have thrown InvalidRequestException due to invalid userId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(USERID_POST_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyCaseRef() {
        try {
            final ViewLog viewLog = new ViewLog();
            viewLog.setCaseRef(randomNumeric(17));

            verifyRequestParamsConditions(viewLog);

            fail("The method should have thrown InvalidRequestException due to invalid caseRef");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASEREF_POST_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyTimestamp() {
        try {
            final ViewLog viewLog = new ViewLog();
            viewLog.setTimestamp("2021-106-23T22:20:05");

            verifyRequestParamsConditions(viewLog);

            fail("The method should have thrown InvalidRequestException due to invalid timestamp");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(TIMESTAMP_POST_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyCaseTypeId() {
        try {
            final ViewLog viewLog = new ViewLog();
            viewLog.setCaseTypeId(randomNumeric(71));

            verifyRequestParamsConditions(viewLog);

            fail("The method should have thrown InvalidRequestException due to invalid case typeId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASETYPEID_POST_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyCaseJurisdictionId() {
        try {
            final ViewLog viewLog = new ViewLog();
            viewLog.setCaseJurisdictionId(randomNumeric(71));

            verifyRequestParamsConditions(viewLog);

            fail("The method should have thrown InvalidRequestException due to invalid jurisdiction id");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASE_JURISDICTION_POST_EXCEPTION_MESSAGE);
        }
    }
}
