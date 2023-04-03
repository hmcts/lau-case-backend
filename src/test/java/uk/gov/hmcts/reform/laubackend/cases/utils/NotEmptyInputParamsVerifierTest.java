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
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyIdNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyRequestActionParamsAreNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyRequestSearchParamsAreNotEmpty;

@SuppressWarnings({"PMD.TooManyMethods","PMD.AvoidDuplicateLiterals"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotEmptyInputParamsVerifierTest {

    @Test
    void shouldVerifyRequestParamsAreNotEmptyForJurisdictionCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                null,
                null,
                null,
                random(71, "123456"),
                "123",
                "456",
                null,
                null)));
    }

    @Test
    void shouldVerifyRequestParamsAreNotEmptyForCaseRefCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                "345",
                null,
                null,
                null,
                "123",
                "456",
                null,
                null)));
    }

    @Test
    void shouldVerifyRequestParamsAreNotEmptyForCaseTypeCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                null,
                "567",
                null,
                null,
                "123",
                "345",
                null,
                null)));
    }

    @Test
    void shouldVerifyRequestParamsAreNotEmptyForActionTypeCaseAction() {
        assertDoesNotThrow(() -> verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                null,
                null,
                "CREATE",
                null,
                "456",
                "123",
                null,
                null)));
    }

    @Test
    void shouldThrowExceptionWhenStartTimeAndEndTimeProvided() {
        try {
            verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                    null,
                    null,
                    null,
                    null,
                    "123",
                    "765",
                    null,
                    null));
            fail("The method should have thrown InvalidRequestException when startTime and endTime input params are "
                    + "provided");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("Both startTime and endTime must be present "
                    + "and at least one of the parameters (userId, caseRef, caseTypeId, caseJurisdictionId, "
                    + "caseAction) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenStartTimeIsMissing() {
        try {
            verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                    "123",
                    null,
                    null,
                    null,
                    null,
                    "765",
                    null,
                    null));
            fail("The method should have thrown InvalidRequestException when startTime  input param is missing");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Both startTime and endTime must be present "
                    + "and at least one of the parameters (userId, caseRef, caseTypeId, caseJurisdictionId, "
                    + "caseAction) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenEndTimeIsMissing() {
        try {
            verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                    "123",
                    null,
                    null,
                    null,
                    "123",
                    null,
                    null,
                    null));
            fail("The method should have thrown InvalidRequestException when endTime input param is missing");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Both startTime and endTime must be present "
                            + "and at least one of the parameters (userId, caseRef, caseTypeId, caseJurisdictionId, "
                            + "caseAction) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenAllParametersAreMissing() {
        try {
            verifyRequestActionParamsAreNotEmpty(new ActionInputParamsHolder(null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null));
            fail("The method should have thrown InvalidRequestException when endTime input param is missing");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Both startTime and endTime must be present "
                            + "and at least one of the parameters (userId, caseRef, caseTypeId, caseJurisdictionId, "
                            + "caseAction) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenRequestParamsAreEmptyForCaseAction() {
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(null,
                    null,
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
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Both startTime and endTime must be present "
                            + "and at least one of the parameters (userId, caseRef, caseTypeId, caseJurisdictionId, "
                            + "caseAction) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenPostRequestParamsAreEmptyForCaseAction() {
        try {
            verifyRequestActionParamsAreNotEmpty(new ActionLog("1",
                    null,
                    null,
                    "4",
                    "5",
                    "6"));
            fail("The method should have thrown InvalidRequestException when all required params are not populated");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("You need to populate all required parameters - "
                            + "userId: 1, action: null, timestamp: 6");
        }
    }

    @Test
    void shouldVerifyRequestParamsAreNotEmptyForCaseSearch() {
        assertDoesNotThrow(() -> verifyRequestSearchParamsAreNotEmpty(new SearchInputParamsHolder(null,
                random(71, "123456"),
                "123",
                "345",
                null,
                null)));
    }

    @Test
    void shouldVerifyRequestParamsAreNotEmptyWhenUserIdNotEmpty() {
        assertDoesNotThrow(() -> verifyRequestSearchParamsAreNotEmpty(new SearchInputParamsHolder("123",
                null,
                "123",
                "345",
                null,
                null)));
    }

    @Test
    void shouldThrowExceptionWhenGetRequestParamsWhenStartTimeEndTimeOnlyPresent() {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(null,
                    null,
                    "123",
                    "4556",
                    null,
                    null);
            verifyRequestSearchParamsAreNotEmpty(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException when all mandatory CaseSearch "
                    + "params are not present");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("Both startTime and endTime must be present "
                    + "and at least one of the parameters (userId, caseRef) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenGetRequestParamsWhenEndTimeEndTimeOnlyPresent() {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder("123",
                    null,
                    null,
                    "4556",
                    null,
                    null);
            verifyRequestSearchParamsAreNotEmpty(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException when all mandatory CaseSearch "
                    + "params are not present");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("Both startTime and endTime must be present "
                    + "and at least one of the parameters (userId, caseRef) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenGetRequestParamsWhenStartTimeOnlyPresent() {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder("123",
                    null,
                    "123",
                    null,
                    null,
                    null);
            verifyRequestSearchParamsAreNotEmpty(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException when all mandatory CaseSearch "
                    + "params are not present");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("Both startTime and endTime must be present "
                    + "and at least one of the parameters (userId, caseRef) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenGetRequestParamsAreEmptyForCaseSearch() {
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
            assertThat(invalidRequestException.getMessage()).isEqualTo("Both startTime and endTime must be present "
                    + "and at least one of the parameters (userId, caseRef) must not be empty");
        }
    }

    @Test
    void shouldThrowExceptionWhenRequestParamsAreEmptyForCaseSearch() {
        try {
            final SearchLog searchLog = new SearchLog("1", asList(random(16, "123456")), null);

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
    void shouldVerifyRequestParamsAreNotEmptyForCaseSearchWithMissingCaseRefs() {
        final SearchLog searchLog = new SearchLog("1", null, "2021-08-23T22:20:05.023Z");

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        assertDoesNotThrow(() -> verifyRequestSearchParamsAreNotEmpty(caseSearchPostRequest));
    }

    @Test
    void shouldThrowExceptionWhenIdIsNotPresent() {
        try {
            verifyIdNotEmpty(null);
            fail("The method should have thrown InvalidRequestException when id is missing");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                    .isEqualTo("Id must be present");
        }
    }
}
