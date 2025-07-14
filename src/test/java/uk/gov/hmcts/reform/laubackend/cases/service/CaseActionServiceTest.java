package uk.gov.hmcts.reform.laubackend.cases.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseActionAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.repository.helpers.QueryBuilder;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseAction.CREATE;


@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"unchecked", "PMD.LawOfDemeter"})
class CaseActionServiceTest {

    @Mock
    private CaseActionAuditRepository caseActionAuditRepository;

    @Mock
    private TimestampUtil timestampUtil;

    @Mock
    private QueryBuilder queryBuilder;

    @InjectMocks
    private CaseActionService caseActionService;

    @Test
    void shouldGetRepositoryResponse() {
        final Timestamp timestamp = mock(Timestamp.class);
        final List<CaseActionAudit> caseActionAuditList = List.of(getCaseViewAuditEntity(timestamp));
        final Page<CaseActionAudit> pageResults = new PageImpl<>(caseActionAuditList);

        setField(caseActionService, "defaultPageSize", "10000");

        final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(
                "1",
                "2",
                "3",
                CREATE.name(),
                "4",
                null,
                null,
                null,
                null);

        final Specification<CaseActionAudit> specification = mock(Specification.class);
        when(queryBuilder.buildCaseActionRequest(inputParamsHolder)).thenReturn(specification);

        when(caseActionAuditRepository
                .findAll(specification,
                        PageRequest.of(0, parseInt("10000"), Sort.by(Sort.Direction.DESC, "timestamp"))))
                .thenReturn(pageResults);

        final CaseActionGetResponse caseView = caseActionService.getCaseView(inputParamsHolder);

        verify(caseActionAuditRepository, times(1))
                .findAll(specification,
                        PageRequest.of(0, parseInt("10000"), Sort.by(Sort.Direction.DESC, "timestamp")));

        assertThat(caseView.getActionLog()).hasSize(1);
        assertThat(caseView.getActionLog().getFirst().getUserId()).isEqualTo("5");
        assertThat(caseView.getActionLog().getFirst().getCaseRef()).isEqualTo("2");
        assertThat(caseView.getActionLog().getFirst().getCaseTypeId()).isEqualTo("3");
        assertThat(caseView.getActionLog().getFirst().getCaseJurisdictionId()).isEqualTo("4");
    }


    @Test
    void shouldPostCaseView() {
        final CaseActionAudit caseActionAudit = new CaseActionAudit();
        caseActionAudit.setCaseActionId(45L);
        caseActionAudit.setUserId("1");
        caseActionAudit.setCaseRef("2");
        caseActionAudit.setCaseJurisdictionId("3");
        caseActionAudit.setCaseTypeId("4");
        caseActionAudit.setCaseAction(CREATE.name());
        caseActionAudit.setTimestamp(Timestamp.valueOf("2021-09-07 14:00:46.852754"));


        final ActionLog actionLog = new ActionLog();
        actionLog.setUserId("1");
        actionLog.setCaseRef("2");
        actionLog.setCaseJurisdictionId("3");
        actionLog.setCaseTypeId("4");
        actionLog.setCaseAction(CREATE.name());
        actionLog.setTimestamp("2021-08-23T22:20:05.023Z");

        when(caseActionAuditRepository.save(any())).thenReturn(caseActionAudit);

        final CaseActionPostResponse caseView = caseActionService.saveCaseAction(actionLog);

        verify(caseActionAuditRepository, times(1)).save(any());
        assertThat(caseView.getActionLog().getUserId()).isEqualTo("1");
        assertThat(caseView.getActionLog().getCaseRef()).isEqualTo("2");
        assertThat(caseView.getActionLog().getCaseJurisdictionId()).isEqualTo("3");
        assertThat(caseView.getActionLog().getCaseTypeId()).isEqualTo("4");
        assertThat(caseView.getActionLog().getCaseAction()).isEqualTo(CREATE.name());
    }

    @Test
    void shouldDeleteCaseSearchId() {
        caseActionService.deleteCaseActionById("1");
        verify(caseActionAuditRepository, times(1)).deleteById(Long.valueOf("1"));
    }


    private CaseActionAudit getCaseViewAuditEntity(final Timestamp timestamp) {
        final CaseActionAudit caseActionAudit = new CaseActionAudit();
        caseActionAudit.setCaseActionId(1L);
        caseActionAudit.setCaseRef("2");
        caseActionAudit.setCaseTypeId("3");
        caseActionAudit.setCaseAction(CREATE.name());
        caseActionAudit.setCaseJurisdictionId("4");
        caseActionAudit.setUserId("5");
        caseActionAudit.setTimestamp(timestamp);
        return caseActionAudit;
    }
}
