package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsAreNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestSearchParamsAreNotEmpty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchInputParamsNotEmptyVerifierTest {

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForCaseSearch() {
        assertDoesNotThrow(() -> verifyRequestSearchParamsAreNotEmpty(new SearchInputParamsHolder(null,
                null,
                randomNumeric(71),

                null,
                null,
                null)));
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
