package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.CASEREF_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.TIMESTAMP_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.USERID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.ExceptionMessageConstants.appendExceptionParameter;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestSearchParamsConditions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchInputParamsVerifierTest {

    @Test
    void shouldVerifyUserId() {
        assertDoesNotThrow(() -> verifyRequestSearchParamsConditions(new SearchInputParamsHolder("3748238",
                null,
                null,
                null,
                null,
                null)));
    }

    @Test
    void shouldNotVerifyUserId() {
        final String userId = randomAlphanumeric(65);
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(userId,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestSearchParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid userId");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(USERID_GET_EXCEPTION_MESSAGE, userId));
        }
    }

    @Test
    void shouldVerifyCaseRef() {
        assertDoesNotThrow(() -> verifyRequestSearchParamsConditions(new SearchInputParamsHolder(null,
                random(16, "123456"),
                null,
                null,
                null,
                null)));
    }

    @Test
    void shouldNotVerifyCaseRef() {
        final String caseRef = random(17, "123456");
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(null,
                    caseRef,
                    null,
                    null,
                    null,
                    null);
            verifyRequestSearchParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid caseRef");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(CASEREF_GET_EXCEPTION_MESSAGE, caseRef));
        }
    }

    @Test
    void shouldVerifyTimestamp() {
        assertDoesNotThrow(() -> verifyRequestSearchParamsConditions(new SearchInputParamsHolder(null,
                null,
                "2021-06-23T22:20:05",
                null,
                null,
                null)));
    }

    @Test
    void shouldNotVerifyTimestamp() {
        final String timestamp = "2021-106-23T22:20:05";
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(null,
                    null,
                    timestamp,
                    null,
                    null,
                    null);
            verifyRequestSearchParamsConditions(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException due to invalid timestamp");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo(appendExceptionParameter(TIMESTAMP_GET_EXCEPTION_MESSAGE, timestamp));
        }
    }

    @Test
    void shouldRemoveInvalidCaseRefsForCaseSearchPost() throws InvalidRequestException {
        final String caseRef = random(16, "123458");
        final List<String> caseRefList = new ArrayList<>(asList(caseRef, "", "null"));
        final SearchLog searchLog = new SearchLog();
        searchLog.setCaseRefs(caseRefList);

        verifyRequestSearchParamsConditions(searchLog);

        assertThat(searchLog.getCaseRefs()).hasSize(1);
        assertThat(searchLog.getCaseRefs().getFirst()).isEqualTo(caseRef);
    }

    @Test
    void shouldRemoveInvalidCaseRefsForCaseSearchPost1() throws InvalidRequestException {
        final String caseRef = random(16, "123459");
        final List<String> caseRefList = new ArrayList<>(asList(caseRef, "123", "567", "null"));
        final SearchLog searchLog = new SearchLog();
        searchLog.setCaseRefs(caseRefList);

        verifyRequestSearchParamsConditions(searchLog);

        assertThat(searchLog.getCaseRefs()).hasSize(1);
        assertThat(searchLog.getCaseRefs().getFirst()).isEqualTo(caseRef);
    }
}
