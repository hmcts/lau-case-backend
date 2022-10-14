package uk.gov.hmcts.reform.laubackend.cases.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchLogTest {

    @Test
    void shouldRemoveDashesAndUnderscoresFromCaseRefList() {
        final String caseRefWithDash = "123-4567891-2345-67";
        final String caseRefWithUnderscore = "123_4567891_2345_88";
        final SearchLog searchLog = new SearchLog();

        searchLog.setCaseRefs(new ArrayList<>(asList(caseRefWithDash,
                caseRefWithUnderscore)));

        assertThat(searchLog.getCaseRefs().size()).isEqualTo(2);
        assertThat(searchLog.getCaseRefs().get(0)).isEqualTo("1234567891234567");
        assertThat(searchLog.getCaseRefs().get(1)).isEqualTo("1234567891234588");
    }

    @Test
    void shouldTrimCaseRefList() {
        final String caseRef1 = "    12345_6789_12345_67     ";
        final String caseRef2 = "  12345678-9123-4588  ";
        final SearchLog searchLog = new SearchLog();

        searchLog.setCaseRefs(new ArrayList<>(asList(caseRef1, caseRef2)));

        assertThat(searchLog.getCaseRefs().size()).isEqualTo(2);
        assertThat(searchLog.getCaseRefs().get(0)).isEqualTo("1234567891234567");
        assertThat(searchLog.getCaseRefs().get(1)).isEqualTo("1234567891234588");
    }
}