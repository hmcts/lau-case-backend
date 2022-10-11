package uk.gov.hmcts.reform.laubackend.cases.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.insights.AppInsights;
import uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseActionService;

import static org.apache.commons.lang3.RandomStringUtils.random;
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
@SuppressWarnings("PMD.LawOfDemeter")
class CaseActionControllerTest {

    @Mock
    private CaseActionService caseActionService;

    @Mock
    private AppInsights appInsights;

    @InjectMocks
    private CaseActionController caseActionController;

    @Captor
    ArgumentCaptor<ActionInputParamsHolder> inputParamsHolderCaptor;

    @Test
    void shouldReturnResponseEntityForGetRequest() {
        final String userId = "1";
        final String caseRef = random(16, "123456");
        final String caseTypeId = "3";
        final String caseAction = "VIEW";
        final CaseActionGetResponse caseActionGetResponse = mock(CaseActionGetResponse.class);

        when(caseActionService.getCaseView(any())).thenReturn(
            caseActionGetResponse);

        final ResponseEntity<CaseActionGetResponse> responseEntity = caseActionController.getCaseAction(
            null,
            null,
            userId,
            caseRef,
            caseTypeId,
            caseAction,
            null,
            null,
            null,
            null,
            null
        );

        verify(caseActionService, times(1)).getCaseView(any(ActionInputParamsHolder.class));
        verify(caseActionService).getCaseView(inputParamsHolderCaptor.capture());
        verify(appInsights, times(1))
            .trackEvent(eq(AppInsightsEvent.GET_ACTIVITY_REQUEST_INFO.toString()), any());
        assertThat(inputParamsHolderCaptor.getValue().getUserId()).isEqualTo(userId);
        assertThat(inputParamsHolderCaptor.getValue().getCaseRef()).isEqualTo(caseRef);
        assertThat(inputParamsHolderCaptor.getValue().getCaseTypeId()).isEqualTo(caseTypeId);
        assertThat(inputParamsHolderCaptor.getValue().getCaseAction()).isEqualTo("VIEW");
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }


    @Test
    void shouldReturnBadRequestResponseEntityForGetRequest() {
        final ResponseEntity<CaseActionGetResponse> responseEntity = caseActionController.getCaseAction(
                null,
                null,
                "1",
                "2",
                "3",
                null,
                null,
                null,
                null,
                null,
                null
        );

        verify(appInsights, times(1))
            .trackEvent(eq(AppInsightsEvent.GET_ACTIVITY_REQUEST_INVALID_REQUEST_EXCEPTION.toString()),anyMap());
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnResponseEntityForPostRequest() {
        final CaseActionPostResponse caseActionPostResponse = mock(CaseActionPostResponse.class);

        when(caseActionService.saveCaseAction(any())).thenReturn(
                caseActionPostResponse);

        final ActionLog actionLog = new ActionLog("1",
                "CREATE",
                "1615817621013640",
                "3",
                "4",
                "2021-08-23T22:20:05.023Z");

        final CaseActionPostRequest caseActionPostRequest = new CaseActionPostRequest();
        caseActionPostRequest.setActionLog(actionLog);

        final ResponseEntity<CaseActionPostResponse> responseEntity = caseActionController.saveCaseAction(
                null,
                caseActionPostRequest
        );

        verify(caseActionService, times(1)).saveCaseAction(actionLog);
        verifyNoInteractions(appInsights); // no telementry for successful posts.
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForPostRequest() {
        final ActionLog actionLog = new ActionLog(null,
                null,
                "2",
                null,
                null,
                null);

        final CaseActionPostRequest caseActionPostRequest = new CaseActionPostRequest();
        caseActionPostRequest.setActionLog(actionLog);
        final ResponseEntity<CaseActionPostResponse> responseEntity = caseActionController.saveCaseAction(
                null,
                caseActionPostRequest

        );

        verify(appInsights, times(1))
            .trackEvent(eq(AppInsightsEvent.POST_ACTIVITY_REQUEST_INVALID_REQUEST_EXCEPTION.toString()),anyMap());
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnInternalServerErrorForPostRequest() {
        final ActionLog actionLog = new ActionLog("1",
                "CREATE",
                "1615817621013640",
                "3",
                "4",
                "2021-08-23T22:20:05.023Z");

        final CaseActionPostRequest caseActionPostRequest = new CaseActionPostRequest();
        caseActionPostRequest.setActionLog(actionLog);

        given(caseActionService.saveCaseAction(any()))
                .willAnswer(invocation -> new Exception("Some terrible exception happened"));

        final ResponseEntity<CaseActionPostResponse> responseEntity = caseActionController.saveCaseAction(
                null,
                caseActionPostRequest
        );

        verify(appInsights, times(1))
            .trackEvent(eq(AppInsightsEvent.POST_ACTIVITY_REQUEST_EXCEPTION.toString()),anyMap());
        assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
}
