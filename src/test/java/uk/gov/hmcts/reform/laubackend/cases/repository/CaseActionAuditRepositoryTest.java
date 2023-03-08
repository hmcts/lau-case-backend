package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseAction.CREATE;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=true"
})
@Import({TimestampUtil.class})
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods"})
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
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
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
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
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
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(1);
        assertResults(caseViewAuditList.getContent(), 4);
    }

    @Test
    void shouldFindCaseByCaseAction() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                "CREATE",
                null,
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(20);
        assertResults(caseViewAuditList.getContent(), 1);
    }

    @Test
    void shouldFindCaseByCaseJurisdictionId() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                null,
                "3",
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
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
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                PageRequest.of(1, 10, Sort.by("log_timestamp")));


        assertThat(caseViewAuditList.getTotalElements()).isEqualTo(20);
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
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(20);
    }

    @Test
    void shouldDeleteCaseActionAudit() {
        caseActionAuditRepository.save(getCaseViewAuditEntity("1",
                "1",
                "1",
                "3333",
                valueOf(now())));


        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                "3333",
                null,
                null,
                null,
                null,
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                null
        );
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(1);

        assertThat(caseViewAuditList.getContent().get(0).getUserId()).isEqualTo("3333");

        caseActionAuditRepository.deleteById(caseViewAuditList.getContent().get(0).getCaseActionId());

        final Page<CaseActionAudit> caseViewAuditList1 = caseActionAuditRepository.findCaseView(
                "3333",
                null,
                null,
                null,
                null,
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                null
        );

        assertThat(caseViewAuditList1.getContent().size()).isEqualTo(0);
    }

    @Test
    void shouldSaveCaseAuctionWithoutCaseJurisdiction() {
        caseActionAuditRepository.save(getCaseViewAuditEntity("1",
                null,
                "4444",
                "6666",
                valueOf(now())));

        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                "6666",
                null,
                null,
                null,
                null,
                valueOf(now().minusDays(50)),
                valueOf(now().plusDays(50)),
                null
        );

        assertThat(caseViewAuditList.getContent().size()).isEqualTo(1);
        assertThat(caseViewAuditList.getContent().get(0).getUserId()).isEqualTo("6666");
        assertThat(caseViewAuditList.getContent().get(0).getCaseTypeId()).isEqualTo("4444");
        assertThat(caseViewAuditList.getContent().get(0).getCaseJurisdictionId()).isEqualTo(null);
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
        caseActionAudit.setCaseAction(CREATE.name());
        caseActionAudit.setCaseJurisdictionId(caseJurisdictionId);
        caseActionAudit.setUserId(userId);
        caseActionAudit.setTimestamp(timestamp);
        return caseActionAudit;
    }
}
