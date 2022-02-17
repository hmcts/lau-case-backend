package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyIdNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyRequestActionParamsAreNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyRequestSearchParamsAreNotEmpty;

@SuppressWarnings("PMD.TooManyMethods")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotEmptyInputParamsVerifierTest {

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForJurisdictionCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                null,
                null,
                randomNumeric(71),
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForCaseRefCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                "345",
                null,
                null,
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForCaseTypeCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                null,
                "567",
                null,
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForStartTimeCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                null,
                null,
                null,
                "678",
                null,
                null,
                null)));
    }

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForEndTimeCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                null,
                null,
                null,
                null,
                "765",
                null,
                null)));
    }

    @Test
    public void shouldThrowExceptionWhenRequestParamsAreEmptyForCaseAction() {
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestActionParamsAreNotEmpty(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException when CaseAction input params are empty");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("At least one path parameter must be present");
        }
    }

    @Test
    public void shouldThrowExceptionWhenPostRequestParamsAreEmptyForCaseAction() {
        try {
            verifyRequestActionParamsAreNotEmpty(new ActionLog("1",
                    "2",
                    null,
                    "4",
                    "5",
                    "6"));
            fail("The method should have thrown InvalidRequestException when all required params are not populated");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("You need to populate all required parameters - "
                            + "userId: 1, action: 2, caseRef: null, caseTypeId: 5, timestamp: 6");
        }
    }


    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForCaseSearch() {
        assertDoesNotThrow(() -> verifyRequestSearchParamsAreNotEmpty(new SearchInputParamsHolder(null,
                randomNumeric(71),
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldThrowExceptionWhenGetRequestParamsAreEmptyForCaseSearch() {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(null,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestSearchParamsAreNotEmpty(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException when all mandatory CaseSearch "
                    + "params are not present");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("At least one path parameter must be present");
        }
    }

    @Test
    public void shouldThrowExceptionWhenRequestParamsAreEmptyForCaseSearch() {
        try {
            final SearchLog searchLog = new SearchLog();
            searchLog.setUserId("1");
            searchLog.setCaseRefs(asList(randomNumeric(16)));

            final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
            caseSearchPostRequest.setSearchLog(searchLog);

            verifyRequestSearchParamsAreNotEmpty(caseSearchPostRequest);
            fail("The method should have thrown InvalidRequestException when input params are empty");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("You need to populate all "
                    + "mandatory parameters - userId: 1, timestamp: null");
        }
    }

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForCaseSearchWithMissingCaseRefs() {
        final SearchLog searchLog = new SearchLog();
        searchLog.setUserId("1");
        searchLog.setTimestamp("2021-08-23T22:20:05.023Z");

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        assertDoesNotThrow(() -> verifyRequestSearchParamsAreNotEmpty(caseSearchPostRequest));
    }

    @Test
    public void shouldThrowExceptionWhenIdIsNotPresent() {
        try {
            verifyIdNotEmpty(null);
            fail("The method should have thrown InvalidRequestException when id is missing");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Id must be present");
        }
    }
}
