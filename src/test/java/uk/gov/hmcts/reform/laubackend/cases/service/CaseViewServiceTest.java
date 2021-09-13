package uk.gov.hmcts.reform.laubackend.cases.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseViewAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsHolder;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CaseViewServiceTest {

    @Mock
    private CaseViewAuditRepository caseViewAuditRepository;

    @InjectMocks
    private CaseViewService caseViewService;

    @Test
    void shouldGetRepositoryResponse() {
        final Timestamp timestamp = mock(Timestamp.class);
        final List<CaseViewAudit> caseViewAuditList = Arrays.asList(getCaseViewAuditEntity(timestamp));
        final Page<CaseViewAudit> pageResults = new PageImpl<>(caseViewAuditList);

        final InputParamsHolder inputParamsHolder = new InputParamsHolder("1","2","3","4",null,null,null,null);


        when(caseViewAuditRepository.findCaseView("1", "2", "3", "4", null, null, null)).thenReturn(pageResults);

        final CaseViewResponse caseView = caseViewService.getCaseView(inputParamsHolder);

        verify(caseViewAuditRepository, times(1)).findCaseView("1", "2", "3", "4", null, null, null);
        assertThat(caseView.getViewLog().size()).isEqualTo(1);
        assertThat(caseView.getViewLog().get(0).getUserId()).isEqualTo("5");
        assertThat(caseView.getViewLog().get(0).getCaseRef()).isEqualTo("2");
        assertThat(caseView.getViewLog().get(0).getCaseTypeId()).isEqualTo("3");
        assertThat(caseView.getViewLog().get(0).getCaseJurisdictionId()).isEqualTo("4");
    }

    private CaseViewAudit getCaseViewAuditEntity(final Timestamp timestamp) {
        final CaseViewAudit caseViewAudit = new CaseViewAudit();
        caseViewAudit.setCaseViewId(1);
        caseViewAudit.setCaseRef("2");
        caseViewAudit.setCaseTypeId("3");
        caseViewAudit.setCaseJurisdictionId("4");
        caseViewAudit.setUserId("5");
        caseViewAudit.setTimestamp(timestamp);
        return caseViewAudit;
    }
}
