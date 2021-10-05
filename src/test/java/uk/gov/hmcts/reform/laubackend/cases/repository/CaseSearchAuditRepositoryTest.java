package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;

import java.sql.Timestamp;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=true"
})
class CaseSearchAuditRepositoryTest {

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;


    @Test
    void shouldSaveCaseSearchAudit() {
        final Timestamp timestamp = valueOf(now());
        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit("1", timestamp);

        final CaseSearchAuditCases caseSearchAuditCases1 = new CaseSearchAuditCases("3", caseSearchAudit);
        final CaseSearchAuditCases caseSearchAuditCases2 = new CaseSearchAuditCases("4", caseSearchAudit);

        caseSearchAudit.addCaseSearchAuditCases(caseSearchAuditCases1);
        caseSearchAudit.addCaseSearchAuditCases(caseSearchAuditCases2);

        caseSearchAuditRepository.save(caseSearchAudit);

        final List<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findAll();

        assertThat(caseSearchAuditList.size()).isEqualTo(1);
        assertThat(caseSearchAuditList.get(0).getTimestamp()).isEqualTo(timestamp);
        assertThat(caseSearchAuditList.get(0).getUserId()).isEqualTo("1");

        assertThat(caseSearchAuditList.get(0).getCaseSearchAuditCases().size()).isEqualTo(2);
        assertThat(caseSearchAuditList.get(0).getCaseSearchAuditCases().get(0).getCaseRef()).isEqualTo("3");
        assertThat(caseSearchAuditList.get(0).getCaseSearchAuditCases().get(1).getCaseRef()).isEqualTo("4");
    }
}