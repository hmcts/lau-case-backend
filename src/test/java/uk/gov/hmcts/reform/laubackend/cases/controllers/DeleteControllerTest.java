package uk.gov.hmcts.reform.laubackend.cases.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseSearchService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteControllerTest {

    @Mock
    private CaseSearchService caseSearchService;

    @InjectMocks
    private DeleteController deleteController;

    @Test
    void shouldReturnResponseEntityForDeleteRequest() {

        doNothing().when(caseSearchService).deleteCaseSearchBbyId("1");

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseSearchRecordById("1");

        verify(caseSearchService, times(1)).deleteCaseSearchBbyId(any());
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }


    @Test
    void shouldReturnBadRequestResponseEntityForDeleteRequest() {

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseSearchRecordById(null);

        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnNotFoundResponseEntityForGetRequest() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(caseSearchService)
                .deleteCaseSearchBbyId("1");

        final ResponseEntity<Object> responseEntity = deleteController.deleteCaseSearchRecordById("1");

        verify(caseSearchService, times(1)).deleteCaseSearchBbyId("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}