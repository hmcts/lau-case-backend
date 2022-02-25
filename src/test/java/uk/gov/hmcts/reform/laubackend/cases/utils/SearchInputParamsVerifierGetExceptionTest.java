package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static com.microsoft.applicationinsights.web.dependencies.apachecommons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestSearchParamsConditions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchInputParamsVerifierGetExceptionTest {

    @Test
    public void shouldNotVerifyUserId() {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(randomAlphanumeric(65),
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestSearchParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid userId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(USERID_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyCaseRef() {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(null,
                    random(17, "123456"),
                    null,
                    null,
                    null,
                    null);
            verifyRequestSearchParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid caseRef");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(CASEREF_GET_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void shouldNotVerifyTimestamp() {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(null,
                    null,
                    "2021-106-23T22:20:05",
                    null,
                    null,
                    null);
            verifyRequestSearchParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid timestamp");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(TIMESTAMP_GET_EXCEPTION_MESSAGE);
        }
    }
}
