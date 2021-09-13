package uk.gov.hmcts.reform.laubackend.cases.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;

import java.sql.Timestamp;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class ViewLogTest {

    @Test
    void shouldCreteDtoFromEntity() {
        final CaseViewAudit caseViewAudit = new CaseViewAudit();
        caseViewAudit.setCaseViewId(1);
        caseViewAudit.setUserId("2");
        caseViewAudit.setCaseTypeId("3");
        caseViewAudit.setTimestamp(Timestamp.valueOf("2021-09-07 14:00:46.852754"));
        caseViewAudit.setCaseRef("4");
        caseViewAudit.setCaseJurisdictionId("5");

        final ViewLog viewLogDto = new ViewLog().toDto(caseViewAudit);

        assertThat(caseViewAudit.getUserId()).isEqualTo(viewLogDto.getUserId());
        assertThat(caseViewAudit.getCaseTypeId()).isEqualTo(viewLogDto.getCaseTypeId());
        assertThat(caseViewAudit.getTimestamp().toString()).isEqualTo(viewLogDto.getTimestamp());
        assertThat(caseViewAudit.getCaseRef()).isEqualTo(viewLogDto.getCaseRef());
        assertThat(caseViewAudit.getCaseJurisdictionId()).isEqualTo(viewLogDto.getCaseJurisdictionId());
        assertThat(caseViewAudit.getUserId()).isEqualTo(viewLogDto.getUserId());
    }
}
