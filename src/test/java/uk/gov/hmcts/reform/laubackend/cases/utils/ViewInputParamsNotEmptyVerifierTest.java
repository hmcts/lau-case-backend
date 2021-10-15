package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestViewParamsAreNotEmpty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewInputParamsNotEmptyVerifierTest {

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForCaseView() {
        assertDoesNotThrow(() -> verifyRequestViewParamsAreNotEmpty(new ViewInputParamsHolder(null,
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
            final ViewInputParamsHolder inputParamsHolder = new ViewInputParamsHolder(null,
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      null);
            verifyRequestViewParamsAreNotEmpty(inputParamsHolder);
            fail("The method should have thrown InvalidRequestException when input params are empty");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage()).isEqualTo("At least one path parameter must be present");
        }
    }
}
