package uk.gov.hmcts.reform.laubackend.cases.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.insights.AppInsights;
import uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.SearchLogPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseSearchService;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidDuplicateLiterals"})
class CaseSearchControllerTest {

    @Mock
    private CaseSearchService caseSearchService;

    @Mock
    private AppInsights appInsights;

    @InjectMocks
    private CaseSearchController caseSearchController;

    @Test
    void shouldReturnResponseEntityForGetRequest() {
        final String userId = "1";
        final String caseRef = random(16, "123456");
        final CaseSearchGetResponse caseSearchGetResponse = mock(CaseSearchGetResponse.class);

        when(caseSearchService.getCaseSearch(any())).thenReturn(
                caseSearchGetResponse);

        final ResponseEntity<CaseSearchGetResponse> responseEntity = caseSearchController.getCaseSearch(
                null,
                null,
                userId,
                caseRef,
                null,
                null,
                null,
                null
        );

        verify(caseSearchService, times(1)).getCaseSearch(any());
        verify(appInsights, times(1))
            .trackEvent(eq(AppInsightsEvent.GET_SEARCH_REQUEST_INFO.toString()), anyMap());
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForGetRequest() {
        final ResponseEntity<CaseSearchGetResponse> responseEntity = caseSearchController.getCaseSearch(
                null,
                null,
                "1",
                "2",
                null,
                null,
                null,
                null
        );

        verify(appInsights, times(1))
            .trackEvent(eq(AppInsightsEvent.GET_SEARCH_REQUEST_INVALID_REQUEST_EXCEPTION.toString()),anyMap());
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }


    @Test
    void shouldReturnResponseEntityForPostRequest() {

        final SearchLog searchLog = new SearchLog("1", asList(random(16, "123456")), "2021-08-23T22:20:05.023Z");
        final SearchLogPostResponse searchLogPostResponse = new SearchLogPostResponse("1",
                "1",
                asList(random(16, "123456")),
                "2021-08-23T22:20:05.023Z");

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest(searchLog);

        final CaseSearchPostResponse caseSearchPostResponse = new CaseSearchPostResponse(searchLogPostResponse);

        when(caseSearchService.saveCaseSearch(any())).thenReturn(
                caseSearchPostResponse);

        final ResponseEntity<CaseSearchPostResponse> responseEntity = caseSearchController.saveCaseSearch(
                caseSearchPostRequest,
                null
        );

        verify(caseSearchService, times(1)).saveCaseSearch(caseSearchPostRequest);
        verifyNoInteractions(appInsights); // no telementry for successful posts.
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForPostRequest() {
        final SearchLog searchLog = new SearchLog("1", asList(random(18, "123456")), null);

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        final ResponseEntity<CaseSearchPostResponse> responseEntity = caseSearchController.saveCaseSearch(
                caseSearchPostRequest,
                null
        );

        verify(appInsights, times(1))
            .trackEvent(eq(AppInsightsEvent.POST_SEARCH_REQUEST_INVALID_REQUEST_EXCEPTION.toString()),anyMap());
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnInternalServerErrorForPostRequest() {
        final SearchLog searchLog = new SearchLog("1",
                asList(random(16, "123456")),
                "2021-08-23T22:20:05.023Z");

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        given(caseSearchService.saveCaseSearch(any()))
                .willAnswer(invocation -> new Exception("Mama mia what a terrible exception"));

        final ResponseEntity<CaseSearchPostResponse> responseEntity = caseSearchController.saveCaseSearch(
                caseSearchPostRequest,
                null
        );

        verify(appInsights, times(1)).trackEvent(
            eq(AppInsightsEvent.POST_SEARCH_REQUEST_EXCEPTION.toString()),anyMap());
        assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

}
