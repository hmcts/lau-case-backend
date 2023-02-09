package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.sql.Timestamp;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.util.CollectionUtils.isEmpty;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=true"
})
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
class CaseSearchAuditRepositoryCaseRefTest {

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;

    @Test
    void shouldRetrieveCasesWithNoCaseRefs() {

        final Timestamp timestamp = valueOf(now().plusDays(1));
        caseSearchAuditRepository.save(getCaseSearchAuditEntity("1", asList(1L, 2L, 3L), timestamp));
        caseSearchAuditRepository.save(getCaseSearchAuditEntity("1", emptyList(), timestamp));
        caseSearchAuditRepository.save(getCaseSearchAuditEntity("2", asList(1L, 2L, 3L), timestamp));

        final Page<CaseSearchAudit> caseSearchAuditList = caseSearchAuditRepository.findCaseSearchH2(
                "1",
                null,
                null,
                null,
                null
        );
        assertThat(caseSearchAuditList.getContent().size()).isEqualTo(2);
        assertThat(caseSearchAuditList.getContent().get(0).getUserId()).isEqualTo("1");
        assertThat(caseSearchAuditList.getContent().get(0).getTimestamp()).isEqualTo(timestamp);
        assertThat(caseSearchAuditList.getContent().get(0).getCaseRefs().size()).isEqualTo(3);
        assertThat(caseSearchAuditList.getContent().get(0).getCaseRefs().get(0)).isEqualTo(1L);
        assertThat(caseSearchAuditList.getContent().get(0).getCaseRefs().get(1)).isEqualTo(2L);
        assertThat(caseSearchAuditList.getContent().get(0).getCaseRefs().get(2)).isEqualTo(3L);

        assertThat(caseSearchAuditList.getContent().get(1).getUserId()).isEqualTo("1");
        assertThat(caseSearchAuditList.getContent().get(1).getCaseRefs().size()).isEqualTo(0);
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
}
