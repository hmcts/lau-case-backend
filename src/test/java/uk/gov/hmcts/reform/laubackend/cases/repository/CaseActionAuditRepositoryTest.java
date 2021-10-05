package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

import java.sql.Timestamp;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=true"
})
class CaseActionAuditRepositoryTest {

    @Autowired
    private CaseActionAuditRepository caseActionAuditRepository;

    @BeforeEach
    public void setUp() {
        //Insert 20 records
        for (int i = 1; i < 21; i++) {
            caseActionAuditRepository
                    .save(getCaseViewAuditEntity(
                            String.valueOf(i),
                            String.valueOf(i),
                            String.valueOf(i),
                            String.valueOf(i),
                            valueOf(now().plusDays(i))
                    ));
        }
    }

    @Test
    void shouldFindCaseByCaseRef() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                "2",
                null,
                null,
                null,
                null,
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(1);
        assertResults(caseViewAuditList.getContent(), 2);
    }

    @Test
    void shouldFindCaseByUserId() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                "10",
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(1);
        assertResults(caseViewAuditList.getContent(), 10);
    }


    @Test
    void shouldFindCaseByCaseTypeId() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                "4",
                null,
                null,
                null,
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(1);
        assertResults(caseViewAuditList.getContent(), 4);
    }

    @Test
    void shouldFindCaseByCaseJurisdictionId() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                "3",
                null,
                null,
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(1);
        assertResults(caseViewAuditList.getContent(), 3);
    }

    @Test
    void shouldFindPageableResults() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(1, 10, Sort.by("timestamp"))
        );

        assertThat(caseViewAuditList.getContent().size()).isEqualTo(10);
    }

    @Test
    void shouldGetAllRecords() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(20);
    }

    private void assertResults(final List<CaseActionAudit> caseActionAuditList, final int value) {
        final String stringValue = String.valueOf(value);
        assertThat(caseActionAuditList.get(0).getCaseRef()).isEqualTo(stringValue);
        assertThat(caseActionAuditList.get(0).getCaseJurisdictionId()).isEqualTo(stringValue);
        assertThat(caseActionAuditList.get(0).getCaseTypeId()).isEqualTo(stringValue);
        assertThat(caseActionAuditList.get(0).getUserId()).isEqualTo(stringValue);
        assertThat(caseActionAuditList.get(0).getAction()).isEqualTo("C");
    }

    private CaseActionAudit getCaseViewAuditEntity(final String caseRef,
                                                   final String caseJurisdictionId,
                                                   final String caseTypeId,
                                                   final String userId,
                                                   final Timestamp timestamp) {
        final CaseActionAudit caseActionAudit = new CaseActionAudit();
        caseActionAudit.setCaseRef(caseRef);
        caseActionAudit.setCaseTypeId(caseTypeId);
        caseActionAudit.setAction("C");
        caseActionAudit.setCaseJurisdictionId(caseJurisdictionId);
        caseActionAudit.setUserId(userId);
        caseActionAudit.setTimestamp(timestamp);
        return caseActionAudit;
    }
}
