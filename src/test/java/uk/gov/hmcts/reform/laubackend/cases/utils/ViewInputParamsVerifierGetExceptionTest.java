package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASETYPEID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASE_JURISDICTION_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestViewParamsConditions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewInputParamsVerifierGetExceptionTest {

    @Test
    public void shouldNotVerifyUserId() {
        try {
            final ViewInputParamsHolder inputParamsHolder = new ViewInputParamsHolder(randomAlphanumeric(65),
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  null);
            verifyRequestViewParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid userId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(USERID_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyCaseRef() {
        try {
            final ViewInputParamsHolder inputParamsHolder = new ViewInputParamsHolder(null,
                    randomNumeric(17),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestViewParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid caseRef");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASEREF_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyTimestamp() {
        try {
            final ViewInputParamsHolder inputParamsHolder = new ViewInputParamsHolder(null,
                    null,
                    null,
                    null,
                    "2021-106-23T22:20:05",
                    null,
                    null,
                    null);
            verifyRequestViewParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid timestamp");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(TIMESTAMP_GET_EXCEPTION_MESSAGE);
        }
    }


    @Test
    public void shouldNotVerifyCaseTypeId() {
        try {
            final ViewInputParamsHolder inputParamsHolder = new ViewInputParamsHolder(null,
                    null,
                    randomNumeric(71),
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestViewParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid case typeId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASETYPEID_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyCaseJurisdictionId() {
        try {
            final ViewInputParamsHolder inputParamsHolder = new ViewInputParamsHolder(null,
                    null,
                    null,
                    randomNumeric(71),
                    null,
                    null,
                    null,
                    null);
            verifyRequestViewParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid jurisdiction id");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASE_JURISDICTION_GET_EXCEPTION_MESSAGE);
        }
    }

}
