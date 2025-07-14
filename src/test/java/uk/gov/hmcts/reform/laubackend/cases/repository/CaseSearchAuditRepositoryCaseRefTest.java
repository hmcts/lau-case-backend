package uk.gov.hmcts.reform.laubackend.cases.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.sql.Timestamp;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.util.CollectionUtils.isEmpty;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=update",
    "spring.liquibase.enabled=false",
    "spring.flyway.enabled=true"
})
class CaseSearchAuditRepositoryCaseRefTest {

    private CaseSearchAuditFindCaseRepository caseSearchAuditFindCaseRepository;

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        caseSearchAuditFindCaseRepository = new CaseSearchAuditFindCaseRepository(entityManager);
    }

    @Test
    void shouldRetrieveCasesWithNoCaseRefs() {
        final Timestamp timestamp = valueOf(now().plusMinutes(1));
        caseSearchAuditRepository.save(getCaseSearchAuditEntity("1", asList(1L, 2L, 3L), timestamp));
        caseSearchAuditRepository.save(getCaseSearchAuditEntity("1", emptyList(), valueOf(now().minusMinutes(2))));
        caseSearchAuditRepository.save(
            getCaseSearchAuditEntity("2", asList(1L, 2L, 3L), valueOf(now().minusMinutes(3))));

        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditFindCaseRepository.findCaseSearch(
                "1",
                null,
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                getPage()
        );
        assertThat(caseSearchAuditList.getContent()).hasSize(2);
        assertThat(caseSearchAuditList.getContent().getFirst().getUserId()).isEqualTo("1");
        assertThat(caseSearchAuditList.getContent().getFirst().getTimestamp()).isEqualTo(timestamp);

        assertThat(caseSearchAuditList.getContent().getFirst().getCaseRefs()).hasSize(3);
        assertThat(caseSearchAuditList.getContent().getFirst().getCaseRefs().get(0)).isEqualTo(1L);
        assertThat(caseSearchAuditList.getContent().getFirst().getCaseRefs().get(1)).isEqualTo(2L);
        assertThat(caseSearchAuditList.getContent().getFirst().getCaseRefs().get(2)).isEqualTo(3L);

        assertThat(caseSearchAuditList.getContent().get(1).getUserId()).isEqualTo("1");
        assertThat(caseSearchAuditList.getContent().get(1).getCaseRefs()).isEmpty();
    }

    private CaseSearchAudit getCaseSearchAuditEntity(final String userId,
                                                     final List<Long> caseRefs,
                                                     final Timestamp timestamp) {
        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit();
        caseSearchAudit.setUserId(userId);
        caseSearchAudit.setTimestamp(timestamp);

        if (!isEmpty(caseRefs)) {
            for (Long caseRef : caseRefs) {
                caseSearchAudit.addCaseRef(caseRef);
            }
        }

        return caseSearchAudit;
    }

    private Pageable getPage() {
        return of(0, 100);
    }
}
