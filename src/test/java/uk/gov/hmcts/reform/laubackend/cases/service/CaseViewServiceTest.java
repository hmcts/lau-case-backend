package uk.gov.hmcts.reform.laubackend.cases.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.InputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseViewAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SuppressWarnings({"PMD.UnusedPrivateField"})
class CaseViewServiceTest {

    @Mock
    private CaseViewAuditRepository caseViewAuditRepository;

    @Mock
    private TimestampUtil timestampUtil;

    @InjectMocks
    private CaseViewService caseViewService;

    @Test
    void shouldGetRepositoryResponse() {
        final Timestamp timestamp = mock(Timestamp.class);
        final List<CaseViewAudit> caseViewAuditList = Arrays.asList(getCaseViewAuditEntity(timestamp));
        final Page<CaseViewAudit> pageResults = new PageImpl<>(caseViewAuditList);

        final InputParamsHolder inputParamsHolder = new InputParamsHolder("1", "2", "3", "4", null, null, null, null);

        when(caseViewAuditRepository
                .findCaseView("1", "2", "3", "4", null, null,
                        PageRequest.of(0, parseInt("10000"), Sort.by("timestamp"))))
                .thenReturn(pageResults);

        final CaseViewGetResponse caseView = caseViewService.getCaseView(inputParamsHolder);

        verify(caseViewAuditRepository, times(1))
                .findCaseView("1", "2", "3", "4", null, null,
                        PageRequest.of(0, parseInt("10000"), Sort.by("timestamp")));

        assertThat(caseView.getViewLog().size()).isEqualTo(1);
        assertThat(caseView.getViewLog().get(0).getUserId()).isEqualTo("5");
        assertThat(caseView.getViewLog().get(0).getCaseRef()).isEqualTo("2");
        assertThat(caseView.getViewLog().get(0).getCaseTypeId()).isEqualTo("3");
        assertThat(caseView.getViewLog().get(0).getCaseJurisdictionId()).isEqualTo("4");
    }


    @Test
    void shouldPostCaseView() {
        final CaseViewAudit caseViewAudit = new CaseViewAudit();

        caseViewAudit.setUserId("1");
        caseViewAudit.setCaseRef("2");
        caseViewAudit.setCaseJurisdictionId("3");
        caseViewAudit.setCaseTypeId("4");
        caseViewAudit.setCaseViewId(5);
        caseViewAudit.setTimestamp(Timestamp.valueOf("2021-09-07 14:00:46.852754"));


        final ViewLog viewLog = new ViewLog();
        viewLog.setUserId("1");
        viewLog.setCaseRef("2");
        viewLog.setCaseJurisdictionId("3");
        viewLog.setCaseTypeId("4");
        viewLog.setTimestamp("2021-08-23T22:20:05.023Z");

        when(caseViewAuditRepository.save(any())).thenReturn(caseViewAudit);

        final CaseViewPostResponse caseView = caseViewService.saveCaseView(viewLog);

        verify(caseViewAuditRepository, times(1)).save(any());
        assertThat(caseView.getViewLog().getCaseViewId()).isEqualTo("5");
        assertThat(caseView.getViewLog().getUserId()).isEqualTo("1");
        assertThat(caseView.getViewLog().getCaseRef()).isEqualTo("2");
        assertThat(caseView.getViewLog().getCaseJurisdictionId()).isEqualTo("3");
        assertThat(caseView.getViewLog().getCaseTypeId()).isEqualTo("4");
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
