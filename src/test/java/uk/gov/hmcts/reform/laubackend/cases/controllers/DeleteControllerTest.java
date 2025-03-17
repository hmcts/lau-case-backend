package uk.gov.hmcts.reform.laubackend.cases.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.service.AccessRequestService;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseActionService;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseSearchService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.TooManyMethods")
class DeleteControllerTest {

    private static final String CASEREF = "1234567890123456";

    @Mock
    private CaseSearchService caseSearchService;

    @Mock
    private CaseActionService caseActionService;

    @Mock
    private AccessRequestService accessRequestService;

    @InjectMocks
    private DeleteController deleteController;

    // Case Search
    @Test
    void shouldReturnResponseEntityForDeleteCaseSearchRequest() {

        doNothing().when(caseSearchService).deleteCaseSearchById("1");

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseSearchRecordById("1");

        verify(caseSearchService, times(1)).deleteCaseSearchById("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForDeleteCaseSearchRequest() {

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseSearchRecordById(null);

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnNotFoundResponseEntityForDeleteCaseRequest() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(caseSearchService)
                .deleteCaseSearchById("1");

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseSearchRecordById("1");

        verify(caseSearchService, times(1)).deleteCaseSearchById("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void shouldReturnInternalServerErrorResponseEntityForDeleteSearchRequest() {
        doThrow(new RuntimeException())
                .when(caseSearchService)
                .deleteCaseSearchById("1");

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseSearchRecordById("1");

        verify(caseSearchService, times(1)).deleteCaseSearchById("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
    // -- Case Search end

    // Case Action
    @Test
    void shouldReturnResponseEntityForDeleteCaseActionRequest() {

        doNothing().when(caseActionService).deleteCaseActionById("1");

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseActionRecordById("1");

        verify(caseActionService, times(1)).deleteCaseActionById("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldReturnBadRequestResponseEntityForDeleteCaseActionRequest() {

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseActionRecordById(null);

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnNotFoundResponseEntityForDeleteActionRequest() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(caseActionService)
                .deleteCaseActionById("1");

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseActionRecordById("1");

        verify(caseActionService, times(1)).deleteCaseActionById("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void shouldReturnInternalServerErrorResponseEntityForDeleteActionRequest() {
        doThrow(new RuntimeException())
                .when(caseActionService)
                .deleteCaseActionById("1");

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseActionRecordById("1");

        verify(caseActionService, times(1)).deleteCaseActionById("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
    // -- Case Action end

    // Access Request
    @Test
    void shouldReturnOkForDeleteAccessRequestRecord() {
        doNothing().when(accessRequestService).deleteAccessRequestRecord("1", CASEREF);

        final ResponseEntity<Object> responseEntity = deleteController.deleteAccessRequestRecord("1", CASEREF);

        verify(accessRequestService, times(1)).deleteAccessRequestRecord("1", CASEREF);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldReturnBadRequestForDeleteAccessRequestRecord() {
        final ResponseEntity<Object> responseEntity = deleteController.deleteAccessRequestRecord(null, null);

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnNotFoundForDeleteAccessRequestRecord() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(accessRequestService)
                .deleteAccessRequestRecord("1", CASEREF);

        final ResponseEntity<Object> responseEntity = deleteController.deleteAccessRequestRecord("1", CASEREF);

        verify(accessRequestService, times(1)).deleteAccessRequestRecord("1", CASEREF);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void shouldReturnInternalServerErrorForDeleteAccessRequestRecord() {
        doThrow(new RuntimeException())
                .when(accessRequestService)
                .deleteAccessRequestRecord("1", CASEREF);

        final ResponseEntity<Object> responseEntity = deleteController.deleteAccessRequestRecord("1", CASEREF);

        verify(accessRequestService, times(1)).deleteAccessRequestRecord("1", CASEREF);
        assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
}
