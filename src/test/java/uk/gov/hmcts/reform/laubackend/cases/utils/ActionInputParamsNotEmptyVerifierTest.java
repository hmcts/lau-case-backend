package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestActionParamsAreNotEmpty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActionInputParamsNotEmptyVerifierTest {

    @Test
    public void shouldVerifyRequestParamsAreNotEmptyForCaseAction() {
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
            fail("The method should have thrown InvalidRequestException when input params are empty");
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
            fail("The method should have thrown InvalidRequestException when input params are empty");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                .isEqualTo("You need to populate all required parameters -"
                               + " userId, action, caseRef, caseJurisdictionId, caseTypeId and timestamp");
        }
    }
}
