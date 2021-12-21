package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=true"
})
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
@Import({RemoveColumnTransformers.class})
class CaseSearchAuditRepositoryTest {

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;

    @BeforeEach
    public void setUp() {
        //Insert 20 records
        for (int i = 1; i < 21; i++) {
            caseSearchAuditRepository
                    .save(getCaseSearchAuditEntity(
                            Arrays.asList(String.valueOf(i)),
                            String.valueOf(i),
                            valueOf(now().plusDays(i))
                    ));
        }
    }

    @Test
    void shouldFindCaseByCaseRef() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearch(
                null,
                "2",
                null,
                null,
                null
        );
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(1);
        assertResults(caseSearchAuditList.getContent(), 2);
    }

    @Test
    void shouldFindCaseByUserId() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearch(
                "10",
                null,
                null,
                null,
                null
        );
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(1);
        assertResults(caseSearchAuditList.getContent(), 10);
    }

    @Test
    void shouldFindPageableResults() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearch(
                null,
                null,
                null,
                null,
                PageRequest.of(1, 10, Sort.by("timestamp"))
        );
        assertThat(caseSearchAuditList.getTotalElements()).isEqualTo(20);
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(10);
    }

    @Test
    void shouldGetAllRecords() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearch(
                null,
                null,
                null,
                null,
                null
        );
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(20);
    }

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

        assertThat(caseSearchAuditList.size()).isEqualTo(21);
        assertThat(caseSearchAuditList.get(20).getTimestamp()).isEqualTo(timestamp);
        assertThat(caseSearchAuditList.get(20).getUserId()).isEqualTo("1");

        assertThat(caseSearchAuditList.get(20).getCaseSearchAuditCases().size()).isEqualTo(2);
        assertThat(caseSearchAuditList.get(20).getCaseSearchAuditCases().get(0).getCaseRef()).isEqualTo("3");
        assertThat(caseSearchAuditList.get(20).getCaseSearchAuditCases().get(1).getCaseRef()).isEqualTo("4");
    }

    private void assertResults(final List<CaseSearchAudit> caseSearchAuditList, final int value) {
        final String stringValue = String.valueOf(value);
        assertThat(caseSearchAuditList.get(0)
                .getCaseSearchAuditCases().get(0).getCaseRef()).isEqualTo(stringValue);
        assertThat(caseSearchAuditList.get(0).getUserId()).isEqualTo(stringValue);
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private CaseSearchAudit getCaseSearchAuditEntity(final List<String> caseRefs,
                                                     final String userId,
                                                     final Timestamp timestamp) {
        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit();
        caseSearchAudit.setUserId(userId);
        caseSearchAudit.setTimestamp(timestamp);
        for (final String caseRefStr : caseRefs) {
            caseSearchAudit.addCaseSearchAuditCases(new CaseSearchAuditCases(caseRefStr, caseSearchAudit));
        }
        return caseSearchAudit;
    }
}
