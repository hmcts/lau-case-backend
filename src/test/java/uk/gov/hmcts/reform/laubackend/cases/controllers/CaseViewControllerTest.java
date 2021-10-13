package uk.gov.hmcts.reform.laubackend.cases.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.request.ViewLogPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseViewService;

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
class CaseViewControllerTest {

    @Mock
    private CaseViewService caseViewService;

    @InjectMocks
    private CaseViewController caseViewController;

    @Test
    void shouldReturnResponseEntityForGetRequest() {
        final String userId = "1";
        final String caseRef = randomNumeric(16);
        final String caseTypeId = "3";
        final CaseViewGetResponse caseViewGetResponse = mock(CaseViewGetResponse.class);

        when(caseViewService.getCaseView(any())).thenReturn(
                caseViewGetResponse);

        final ResponseEntity<CaseViewGetResponse> responseEntity = caseViewController.getCaseView(
                userId,
                caseRef,
                caseTypeId,
                null,
                null,
                null,
                null,
                null
        );

        verify(caseViewService, times(1)).getCaseView(any());
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForGetRequest() {
        final ResponseEntity<CaseViewGetResponse> responseEntity = caseViewController.getCaseView(
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
        final CaseViewPostResponse caseViewPostResponse = mock(CaseViewPostResponse.class);

        when(caseViewService.saveCaseView(any())).thenReturn(
                caseViewPostResponse);

        final ViewLog viewLog = new ViewLog();
        viewLog.setUserId("1");
        viewLog.setCaseRef(randomNumeric(16));

        final ViewLogPostRequest viewLogPostRequest = new ViewLogPostRequest();
        viewLogPostRequest.setViewLog(viewLog);

        final ResponseEntity<CaseViewPostResponse> responseEntity = caseViewController.saveCaseView(
                viewLogPostRequest
        );

        verify(caseViewService, times(1)).saveCaseView(viewLog);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForPostRequest() {
        final ViewLog viewLog = new ViewLog();
        viewLog.setCaseRef("2");

        final ViewLogPostRequest viewLogPostRequest = new ViewLogPostRequest();
        viewLogPostRequest.setViewLog(viewLog);
        final ResponseEntity<CaseViewPostResponse> responseEntity = caseViewController.saveCaseView(
                viewLogPostRequest
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnInternalServerErrorForPostRequest() {
        final ViewLog viewLog = new ViewLog();
        viewLog.setUserId("1");

        final ViewLogPostRequest viewLogPostRequest = new ViewLogPostRequest();
        viewLogPostRequest.setViewLog(viewLog);

        given(caseViewService.saveCaseView(any()))
                .willAnswer(invocation -> new Exception("Some terrible exception happened"));

        final ResponseEntity<CaseViewPostResponse> responseEntity = caseViewController.saveCaseView(
                viewLogPostRequest
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
}