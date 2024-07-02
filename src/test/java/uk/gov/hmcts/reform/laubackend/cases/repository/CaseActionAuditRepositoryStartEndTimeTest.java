package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.repository.helpers.QueryBuilder;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.PageRequest.of;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseAction.CREATE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=update",
    "spring.liquibase.enabled=false",
    "spring.flyway.enabled=true"
})
@Import({QueryBuilder.class, TimestampUtil.class})
class CaseActionAuditRepositoryStartEndTimeTest {

    @Autowired
    private CaseActionAuditRepository caseActionAuditRepository;

    @Autowired
    private QueryBuilder queryBuilder;

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
    void shouldFindCaseByStartTime1() {
        final ActionInputParamsHolder actionInputParamsHolder = new ActionInputParamsHolder(null,
                null,
                null,
                null,
                null,
                now().plusDays(10).toString(),
                now().plusDays(30).toString(),
                null,
                null);

        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository
                .findAll(queryBuilder.buildCaseActionRequest(actionInputParamsHolder), getPage());

        //Will return 10 days because  the date start is +10 from now
        assertThat(caseViewAuditList.getContent()).hasSize(10);
    }

    @Test
    void shouldFindCaseByEndTime2() {
        final ActionInputParamsHolder actionInputParamsHolder = new ActionInputParamsHolder(null,
                "1",
                null,
                null,
                null,
                now().toString(),
                now().plusDays(1).toString(),
                null,
                null);

        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository
                .findAll(queryBuilder.buildCaseActionRequest(actionInputParamsHolder), getPage());

        assertThat(caseViewAuditList.getContent()).hasSize(1);
        assertResults(caseViewAuditList.getContent(), 1);
    }

    @Test
    void shouldNotFindCaseByStartTimeEndTime1() {
        final ActionInputParamsHolder actionInputParamsHolder = new ActionInputParamsHolder(null,
                "10",
                null,
                null,
                null,
                now().plusDays(20).toString(),
                now().plusDays(30).toString(),
                null,
                null);

        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository
                .findAll(queryBuilder.buildCaseActionRequest(actionInputParamsHolder), getPage());

        assertThat(caseViewAuditList.getContent()).isEmpty();
    }

    @Test
    void shouldNotFindCaseByStartTimeEndTime2() {

        final ActionInputParamsHolder actionInputParamsHolder = new ActionInputParamsHolder(null,
                "10",
                null,
                null,
                null,
                now().minusDays(2).toString(),
                now().minusDays(1).toString(),
                null,
                null);

        final Page<CaseActionAudit> caseViewAuditList = caseActionAuditRepository
                .findAll(queryBuilder.buildCaseActionRequest(actionInputParamsHolder), getPage());

        assertThat(caseViewAuditList.getContent()).isEmpty();
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


    private Pageable getPage() {
        return of(0, 100);
    }
}
