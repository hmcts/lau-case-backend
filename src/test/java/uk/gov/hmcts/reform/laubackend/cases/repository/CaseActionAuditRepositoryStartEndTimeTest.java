package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

import java.sql.Timestamp;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseAction.CREATE;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=true"
})
class CaseActionAuditRepositoryStartEndTimeTest {

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
    void shouldFindCaseByStartTime() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                null,
                valueOf(now().plusDays(10)),
                null,
                null
        );
        //Will return 10 days because  the date start is +10 from now
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(10);
    }

    @Test
    void shouldFindCaseByEndTime() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                "1",
                null,
                null,
                null,
                null,
                valueOf(now().plusDays(1)),
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(1);
        assertResults(caseViewAuditList.getContent(), 1);
    }

    @Test
    void shouldNotFindCaseByStartTime() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                "10",
                null,
                null,
                null,
                valueOf(now().plusDays(20)),
                null,
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(0);
    }

    @Test
    void shouldNotFindCaseByEndTime() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                "10",
                null,
                null,
                null,
                null,
                valueOf(now().minusDays(1)),
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(0);
    }

    @Test
    void shouldNotFindCaseByStartTimeAndEndTime() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                null,
                valueOf(now().minusDays(1)),
                valueOf(now().minusDays(2)),
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(0);
    }


    private void assertResults(final List<CaseActionAudit> caseActionAuditList, final int value) {
        final String stringValue = String.valueOf(value);
        assertThat(caseActionAuditList.get(0).getCaseRef()).isEqualTo(stringValue);
        assertThat(caseActionAuditList.get(0).getCaseJurisdictionId()).isEqualTo(stringValue);
        assertThat(caseActionAuditList.get(0).getCaseTypeId()).isEqualTo(stringValue);
        assertThat(caseActionAuditList.get(0).getUserId()).isEqualTo(stringValue);
        assertThat(caseActionAuditList.get(0).getCaseAction()).isEqualTo(CREATE.name());
    }

    private CaseActionAudit getCaseViewAuditEntity(final String caseRef,
                                                   final String caseJurisdictionId,
                                                   final String caseTypeId,
                                                   final String userId,
                                                   final Timestamp timestamp) {
        final CaseActionAudit caseActionAudit = new CaseActionAudit();
        caseActionAudit.setCaseRef(caseRef);
        caseActionAudit.setCaseTypeId(caseTypeId);
        caseActionAudit.setCaseJurisdictionId(caseJurisdictionId);
        caseActionAudit.setCaseAction(CREATE.name());
        caseActionAudit.setUserId(userId);
        caseActionAudit.setTimestamp(timestamp);
        return caseActionAudit;
    }
}
