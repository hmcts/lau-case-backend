package uk.gov.hmcts.reform.laubackend.cases.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseSearchService;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CaseSearchControllerTest {

    @Mock
    private CaseSearchService caseSearchService;

    @InjectMocks
    private CaseSearchController caseSearchController;


    @Test
    void shouldReturnResponseEntityForPostRequest() {

        final SearchLog searchLog = new SearchLog();
        searchLog.setUserId("1");
        searchLog.setCaseRefs(asList(randomNumeric(16)));
        searchLog.setTimestamp("2021-08-23T22:20:05.023Z");

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        when(caseSearchService.saveCaseSearch(any())).thenReturn(
                caseSearchPostRequest);

        final ResponseEntity<CaseSearchPostRequest> responseEntity = caseSearchController.saveCaseSearch(
                caseSearchPostRequest
        );

        verify(caseSearchService, times(1)).saveCaseSearch(caseSearchPostRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForPostRequest() {
        final SearchLog searchLog = new SearchLog();
        searchLog.setUserId("1");
        // Validation will fail here
        searchLog.setCaseRefs(asList(randomNumeric(18)));

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        final ResponseEntity<CaseSearchPostRequest> responseEntity = caseSearchController.saveCaseSearch(
                caseSearchPostRequest
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnInternalServerErrorForPostRequest() {
        final SearchLog searchLog = new SearchLog();
        searchLog.setUserId("1");
        searchLog.setCaseRefs(asList(randomNumeric(16)));
        searchLog.setTimestamp("2021-08-23T22:20:05.023Z");

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        given(caseSearchService.saveCaseSearch(any()))
                .willAnswer(invocation -> new Exception("Mama mia what a terrible exception"));

        final ResponseEntity<CaseSearchPostRequest> responseEntity = caseSearchController.saveCaseSearch(
                caseSearchPostRequest
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

}