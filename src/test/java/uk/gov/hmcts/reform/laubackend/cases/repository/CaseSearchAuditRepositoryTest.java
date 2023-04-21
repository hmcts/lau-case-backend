package uk.gov.hmcts.reform.laubackend.cases.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.data.domain.PageRequest.of;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=update",
    "spring.liquibase.enabled=false",
    "spring.flyway.enabled=true"
})
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "PMD.AvoidDuplicateLiterals"})
class CaseSearchAuditRepositoryTest {

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;

    private CaseSearchAuditFindCaseRepository caseSearchAuditFindCaseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        caseSearchAuditFindCaseRepository = new CaseSearchAuditFindCaseRepository(entityManager);

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
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditFindCaseRepository.findCaseSearch(
                null,
                "2",
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                getPage()
        );
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(1);
        assertThat(caseSearchAuditList.getContent().get(0).getCaseRefs().get(0)).isEqualTo(2L);
    }

    @Test
    void shouldFindCaseByUserId() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditFindCaseRepository.findCaseSearch(
                "10",
                null,
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                getPage()
        );
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(1);
        assertThat(caseSearchAuditList.getContent().get(0).getUserId()).isEqualTo("10");
    }

    @Test
    void shouldFindPageableResults() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditFindCaseRepository.findCaseSearch(
                null,
                null,
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                of(1, 10, Sort.by("log_timestamp"))
        );
        assertThat(caseSearchAuditList.getTotalElements()).isEqualTo(20);
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(10);
    }

    @Test
    void shouldGetAllRecords() {
        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditFindCaseRepository.findCaseSearch(
                null,
                null,
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                getPage()
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

        final Page<CaseSearchAudit> caseSearch = caseSearchAuditFindCaseRepository
                .findCaseSearch("3333",
                        null,
                        valueOf(now().minusDays(50)),
                        valueOf(now().plusDays(50)),
                        getPage());

        assertThat(caseSearch.getContent().size()).isEqualTo(1);
        assertThat(caseSearch.getContent().get(0).getUserId()).isEqualTo("3333");

        caseSearchAuditRepository.deleteById(caseSearch.getContent().get(0).getId());

        final Page<CaseSearchAudit> caseSearch1 = caseSearchAuditFindCaseRepository
                .findCaseSearch("3333",
                        null,
                        valueOf(now().minusDays(50)),
                        valueOf(now().plusDays(50)), getPage());

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

    private Pageable getPage() {
        return of(0, 100);
    }
}
