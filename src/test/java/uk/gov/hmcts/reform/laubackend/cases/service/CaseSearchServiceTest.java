package uk.gov.hmcts.reform.laubackend.cases.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseSearchAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SuppressWarnings({"PMD.UnusedPrivateField"})
class CaseSearchServiceTest {

    @Mock
    private CaseSearchAuditRepository caseSearchAuditRepository;

    @Mock
    private TimestampUtil timestampUtil;

    @InjectMocks
    private CaseSearchService caseSearchService;

    @Test
    void shouldPostCaseView() {

        final String caseRef = randomNumeric(16);

        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit();
        caseSearchAudit.setUserId("1");
        caseSearchAudit.setTimestamp(Timestamp.valueOf("2021-09-07 14:00:46.852754"));

        final CaseSearchAuditCases caseSearchAuditCases = new CaseSearchAuditCases();
        caseSearchAuditCases.setId(Long.valueOf(1));

        caseSearchAuditCases.setCaseRef(caseRef);
        caseSearchAudit.setCaseSearchAuditCases(List.of(caseSearchAuditCases));

        final SearchLog searchLog = new SearchLog();
        searchLog.setUserId("1");
        searchLog.setCaseRefs(asList(caseRef));

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        when(caseSearchAuditRepository.save(any())).thenReturn(caseSearchAudit);

        final CaseSearchPostRequest searchPostRequest = caseSearchService.saveCaseSearch(caseSearchPostRequest);

        verify(caseSearchAuditRepository, times(1)).save(any());
        assertThat(searchPostRequest.getSearchLog().getUserId()).isEqualTo("1");
        assertThat(searchPostRequest.getSearchLog().getCaseRefs().get(0)).isEqualTo(caseRef);

    }
}