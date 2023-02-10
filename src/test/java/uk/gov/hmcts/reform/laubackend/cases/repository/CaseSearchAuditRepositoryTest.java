package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

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
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops","PMD.AvoidDuplicateLiterals"})
class CaseSearchAuditRepositoryTest {

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;

    @BeforeEach
    public void setUp() {
        //Insert 20 records
        for (int i = 1; i < 21; i++) {
            caseSearchAuditRepository
                    .save(getCaseSearchAuditEntity(
                            Arrays.asList(Long.valueOf(i)),
                            String.valueOf(i),
                            valueOf(now().plusDays(i))
                    ));
        }
    }

    @Test
    void shouldFindCaseByCaseRef() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearchH2(
                null,
                "2",
                null,
                null,
                null
        );
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(1);
        assertThat(caseSearchAuditList.getContent().get(0).getCaseRefs().get(0)).isEqualTo(2L);
    }

    @Test
    void shouldFindCaseByUserId() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearchH2(
                "10",
                null,
                null,
                null,
                null
        );
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(1);
        assertThat(caseSearchAuditList.getContent().get(0).getUserId()).isEqualTo("10");
    }

    @Test
    void shouldFindPageableResults() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearchH2(
                null,
                null,
                null,
                null,
                PageRequest.of(1, 10, Sort.by("log_timestamp"))
        );
        assertThat(caseSearchAuditList.getTotalElements()).isEqualTo(20);
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(10);
    }

    @Test
    void shouldGetAllRecords() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearchH2(
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
        List<Long> caseRefs = Arrays.asList(3L, 4L);
        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit("1", timestamp, caseRefs);

        caseSearchAuditRepository.save(caseSearchAudit);

        final List<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findAll();

        assertThat(caseSearchAuditList.size()).isEqualTo(21);
        assertThat(caseSearchAuditList.get(20).getTimestamp()).isEqualTo(timestamp);
        assertThat(caseSearchAuditList.get(20).getUserId()).isEqualTo("1");

        assertThat(caseSearchAuditList.get(20).getCaseRefs().size()).isEqualTo(2);
        assertThat(caseSearchAuditList.get(20).getCaseRefs().get(0)).isEqualTo(3L);
        assertThat(caseSearchAuditList.get(20).getCaseRefs().get(1)).isEqualTo(4L);
    }

    @Test
    void shouldDeleteCaseSearchAudit() {
        final Timestamp timestamp = valueOf(now());
        List<Long> caseRefs = Arrays.asList(3L);
        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit("3333", timestamp, caseRefs);

        caseSearchAuditRepository.save(caseSearchAudit);

        final Page<CaseSearchAudit> caseSearch = caseSearchAuditRepository
                .findCaseSearchH2("3333", null, null, null, null);

        assertThat(caseSearch.getContent().size()).isEqualTo(1);
        assertThat(caseSearch.getContent().get(0).getUserId()).isEqualTo("3333");

        caseSearchAuditRepository.deleteById(caseSearch.getContent().get(0).getId());

        final Page<CaseSearchAudit> caseSearch1 = caseSearchAuditRepository
                .findCaseSearchH2("3333", null, null, null, null);

        assertThat(caseSearch1.getContent().size()).isEqualTo(0);
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private CaseSearchAudit getCaseSearchAuditEntity(final List<Long> caseRefs,
                                                     final String userId,
                                                     final Timestamp timestamp) {
        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit();
        caseSearchAudit.setUserId(userId);
        caseSearchAudit.setTimestamp(timestamp);

        for (final Long caseRef : caseRefs) {
            caseSearchAudit.addCaseRef(caseRef);
        }
        return caseSearchAudit;
    }
}
