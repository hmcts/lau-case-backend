package uk.gov.hmcts.reform.laubackend.cases.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseActionService;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CaseActionControllerTest {

    @Mock
    private CaseActionService caseActionService;

    @InjectMocks
    private CaseActionController caseActionController;

    @Test
    void shouldReturnResponseEntityForGetRequest() {
        final String userId = "1";
        final String caseRef = randomNumeric(16);
        final String caseTypeId = "3";
        final CaseActionGetResponse caseActionGetResponse = mock(CaseActionGetResponse.class);

        when(caseActionService.getCaseView(any())).thenReturn(
                caseActionGetResponse);

        final ResponseEntity<CaseActionGetResponse> responseEntity = caseActionController.getCaseView(
                userId,
                caseRef,
                caseTypeId,
                null,
                null,
                null,
                null,
                null
        );

        verify(caseActionService, times(1)).getCaseView(any());
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForGetRequest() {
        final ResponseEntity<CaseActionGetResponse> responseEntity = caseActionController.getCaseView(
                "1",
                "2",
                "3",
                null,
                null,
                null,
                null,
                null
        );

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
                caseActionPostRequest
        );

        verify(caseActionService, times(1)).saveCaseAction(actionLog);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForPostRequest() {
        final ActionLog actionLog = new ActionLog();
        actionLog.setCaseRef("2");

        final CaseActionPostRequest caseActionPostRequest = new CaseActionPostRequest();
        caseActionPostRequest.setActionLog(actionLog);
        final ResponseEntity<CaseActionPostResponse> responseEntity = caseActionController.saveCaseAction(
                caseActionPostRequest
        );

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
                caseActionPostRequest
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
}