package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;

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
    void shouldFindCaseByStartTimeEndTime() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                null,
                null,
                valueOf(now().plusDays(10)),
                valueOf(now().plusDays(30)),
                null
        );
        //Will return 10 days because  the date start is +10 from now
        assertThat(caseViewAuditList.getContent().size()).isEqualTo(10);
    }


    @Test
    void shouldNotFindCaseByEndTime() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                "10",
                null,
                null,
                null,
                null,
                valueOf(now().minusDays(2)),
                valueOf(now().minusDays(1)),
                null);

        assertThat(caseViewAuditList.getContent().size()).isEqualTo(0);
    }

    @Test
    void shouldNotFindCaseByStartTimeAndEndTime() {
        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository.findCaseView(
                null,
                null,
                null,
                null,
                null,
                valueOf(now().minusDays(1)),
                valueOf(now().minusDays(2)),
                null);

        assertThat(caseViewAuditList.getContent().size()).isEqualTo(0);
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
