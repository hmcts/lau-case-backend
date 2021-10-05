package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.InputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsAreNotEmpty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InputParamsNotEmptyVerifierTest {

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForCaseView() {
        assertDoesNotThrow(() -> verifyRequestParamsAreNotEmpty(new InputParamsHolder(null,
                null,
                null,
                randomNumeric(71),
                null,
                null,
                null,
                null)));
    }

    @Test
    public void shouldThrowExceptionWhenRequestParamsAreEmptyForCaseView() {
        try {
            final InputParamsHolder inputParamsHolder = new InputParamsHolder(null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            verifyRequestParamsAreNotEmpty(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException when input params are empty");
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

            verifyRequestParamsAreNotEmpty(caseSearchPostRequest);
            fail("The method should have thrown InvalidRequestException when input params are empty");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("You need to populate all parameters - "
                    + "userId, caseRefs and timestamp");
        }
    }
}
