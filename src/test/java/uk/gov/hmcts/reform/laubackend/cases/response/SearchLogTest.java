package uk.gov.hmcts.reform.laubackend.cases.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;

import java.sql.Timestamp;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class SearchLogTest {

    @Test
    void shouldCreteDtoFromEntity() {
        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit();
        caseSearchAudit.setUserId("1");
        caseSearchAudit.setTimestamp(Timestamp.valueOf("2021-09-07 14:00:46.852754"));
        final CaseSearchAuditCases caseViewAuditCases = new CaseSearchAuditCases("2", caseSearchAudit);
        caseSearchAudit.addCaseSearchAuditCases(caseViewAuditCases);

        final SearchLog searchLogDto = new SearchLog().toDto(caseSearchAudit, "2021-09-07 14:00:46.852754");

        assertThat(caseSearchAudit.getUserId()).isEqualTo(searchLogDto.getUserId());
        assertThat(caseSearchAudit.getTimestamp().toString()).isEqualTo("2021-09-07 14:00:46.852754");
        assertThat(caseSearchAudit.getCaseSearchAuditCases().get(0).getCaseRef())
            .isEqualTo(searchLogDto.getCaseRefs().get(0));
        assertThat(caseSearchAudit.getUserId()).isEqualTo(searchLogDto.getUserId());
    }
}
