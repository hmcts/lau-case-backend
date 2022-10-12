package uk.gov.hmcts.reform.laubackend.cases.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActionLogTest {

    @Test
    void shouldRemoveDashesFromCaseRef() {
        final String caseRefWithDash = "123-4567891-2345-67";
        final ActionLog searchLog = new ActionLog(null, null, caseRefWithDash,
                null, null, null);
        assertThat(searchLog.getCaseRef()).isEqualTo("1234567891234567");
    }

    @Test
    void shouldRemoveUnderscoresFromCaseRef() {
        final String caseRefWithDash = "123_4567891-2345_67";
        final ActionLog searchLog = new ActionLog(null, null, caseRefWithDash,
                null, null, null);
        assertThat(searchLog.getCaseRef()).isEqualTo("1234567891234567");
    }

    @Test
    void shouldTrimCaseRef() {
        final String caseRef = "        1234567891234567     ";
        final ActionLog searchLog = new ActionLog(null, null, caseRef,
                null, null, null);
        assertThat(searchLog.getCaseRef()).isEqualTo("1234567891234567");
    }


}