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
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseSearchAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

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
    void shouldGetRepositoryResponse() {
        final Timestamp timestamp = mock(Timestamp.class);
        final List<CaseSearchAudit> caseSearchAuditList = asList(getCaseSearchAuditEntity(timestamp));
        final Page<CaseSearchAudit> pageResults = new PageImpl<>(caseSearchAuditList);

        setField(caseSearchService, "defaultPageSize", "10000");

        final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(
                "1",
                "2",
                "3",
                "4",
                null,
                null);

        when(caseSearchAuditRepository
                .findCaseSearch("1", "2", null, null,
                        PageRequest.of(0, parseInt("10000"), Sort.by("log_timestamp"))))
                .thenReturn(pageResults);

        final CaseSearchGetResponse caseSearch = caseSearchService.getCaseSearch(inputParamsHolder);

        verify(caseSearchAuditRepository, times(1))
                .findCaseSearch("1", "2", null, null,
                        PageRequest.of(0, parseInt("10000"), Sort.by("log_timestamp")));

        assertThat(caseSearch.getSearchLog().size()).isEqualTo(1);
        assertThat(caseSearch.getSearchLog().get(0).getUserId()).isEqualTo("1");
        assertThat(caseSearch.getSearchLog().get(0).getCaseRefs().get(0)).isEqualTo("2");
    }

    @Test
    void shouldPostCaseView() {

        final String caseRef = random(16, "123456");

        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit();
        caseSearchAudit.setId(Long.valueOf(3));
        caseSearchAudit.setUserId("1");
        caseSearchAudit.setTimestamp(Timestamp.valueOf("2021-09-07 14:00:46.852754"));
        caseSearchAudit.setCaseRefs(List.of(Long.valueOf(caseRef)));

        final SearchLog searchLog = new SearchLog();
        searchLog.setUserId("1");
        searchLog.setCaseRefs(asList(caseRef));

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        when(caseSearchAuditRepository.save(any())).thenReturn(caseSearchAudit);

        final CaseSearchPostResponse caseSearchPostResponse = caseSearchService.saveCaseSearch(caseSearchPostRequest);

        verify(caseSearchAuditRepository, times(1)).save(any());
        assertThat(caseSearchPostResponse.getSearchLog().getId()).isEqualTo("3");
        assertThat(caseSearchPostResponse.getSearchLog().getUserId()).isEqualTo("1");
        assertThat(caseSearchPostResponse.getSearchLog().getCaseRefs().get(0)).isEqualTo(caseRef);
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private CaseSearchAudit getCaseSearchAuditEntity(final Timestamp timestamp) {
        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit();
        caseSearchAudit.setUserId("1");
        caseSearchAudit.addCaseRef(2L);
        caseSearchAudit.setTimestamp(timestamp);
        return caseSearchAudit;
    }

    @Test
    void shouldDeleteCaseSearchId() {
        caseSearchService.deleteCaseSearchBbyId("1");
        verify(caseSearchAuditRepository, times(1)).deleteById(Long.valueOf("1"));
    }
}
