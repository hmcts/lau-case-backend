package uk.gov.hmcts.reform.laubackend.cases.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseViewService;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuditControllerTest {

    @Mock
    private CaseViewService caseViewService;

    @InjectMocks
    private AuditController auditController;

    @Test
    void shouldReturnResponseEntity() {
        final String userId = "1";
        final String caseRef = randomNumeric(16);
        final String caseTypeId = "3";
        final CaseViewResponse caseViewResponse = mock(CaseViewResponse.class);

        when(caseViewService.getCaseView(any())).thenReturn(
            caseViewResponse);

        final ResponseEntity<CaseViewResponse> responseEntity = auditController.getCaseView(
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
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnBadRequestResponseEntity() {
        final String userId = "1";
        final String caseRef = "2";
        final String caseTypeId = "3";

        final ResponseEntity<CaseViewResponse> responseEntity = auditController.getCaseView(
            userId,
            caseRef,
            caseTypeId,
            null,
            null,
            null,
            null,
            null
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
