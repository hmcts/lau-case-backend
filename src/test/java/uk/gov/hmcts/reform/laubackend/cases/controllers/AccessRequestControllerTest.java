package uk.gov.hmcts.reform.laubackend.cases.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.AccessRequestPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.AccessRequestService;

import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessRequestControllerTest {

    @Mock
    private AccessRequestService accessRequestService;

    @InjectMocks
    private AccessRequestController accessRequestController;

    @Test
    void shouldReturnResponseEntityForPostRequest() {
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(AccessRequestType.CHALLENGED)
            .userId("1234-abcd")
            .build();

        when(accessRequestService.save(any())).thenReturn(accessRequestLog);
        AccessRequestPostRequest request = AccessRequestPostRequest.builder().accessLog(accessRequestLog).build();

        ResponseEntity<AccessRequestPostResponse> response =
            accessRequestController.saveAccessRequest(null, request);
        verify(accessRequestService, times(1)).save(accessRequestLog);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnBadRequestForBadRequestBody() {
        AccessRequestPostRequest request = AccessRequestPostRequest.builder()
            .accessLog(AccessRequestLog.builder().timestamp("abc").build())
            .build();
        when(accessRequestService.save(any())).thenThrow(new DateTimeParseException("Test exception", "abc", 0));

        ResponseEntity<AccessRequestPostResponse> response =
            accessRequestController.saveAccessRequest(null, request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(accessRequestService, times(1)).save(any());
    }

    @Test
    void shouldReturnInternalServerErrorForBadPostRequest() {
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(AccessRequestType.CHALLENGED)
            .userId("1234-abcd")
            .build();

        when(accessRequestService.save(any())).thenThrow(new RuntimeException("Test exception"));
        AccessRequestPostRequest request = AccessRequestPostRequest.builder().accessLog(accessRequestLog).build();

        ResponseEntity<AccessRequestPostResponse> response =
            accessRequestController.saveAccessRequest(null, request);
        verify(accessRequestService, times(1)).save(accessRequestLog);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
