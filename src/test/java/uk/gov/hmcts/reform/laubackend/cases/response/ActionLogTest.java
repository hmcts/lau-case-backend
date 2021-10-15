package uk.gov.hmcts.reform.laubackend.cases.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;

import java.sql.Timestamp;

import static java.lang.Long.valueOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class ActionLogTest {

    @Test
    void shouldCreteDtoFromEntity() {
        final CaseActionAudit caseActionAudit = new CaseActionAudit();
        caseActionAudit.setCaseActionId(valueOf(1));
        caseActionAudit.setUserId("2");
        caseActionAudit.setCaseTypeId("3");
        caseActionAudit.setTimestamp(Timestamp.valueOf("2021-09-07 14:00:46.852754"));
        caseActionAudit.setCaseRef("4");
        caseActionAudit.setCaseJurisdictionId("5");

        final ActionLog actionLogDto = new ActionLog().toDto(caseActionAudit, "2021-09-07 14:00:46.852754");

        assertThat(caseActionAudit.getUserId()).isEqualTo(actionLogDto.getUserId());
        assertThat(caseActionAudit.getCaseTypeId()).isEqualTo(actionLogDto.getCaseTypeId());
        assertThat(caseActionAudit.getTimestamp().toString()).isEqualTo("2021-09-07 14:00:46.852754");
        assertThat(caseActionAudit.getCaseRef()).isEqualTo(actionLogDto.getCaseRef());
        assertThat(caseActionAudit.getCaseJurisdictionId()).isEqualTo(actionLogDto.getCaseJurisdictionId());
        assertThat(caseActionAudit.getUserId()).isEqualTo(actionLogDto.getUserId());
    }
}
