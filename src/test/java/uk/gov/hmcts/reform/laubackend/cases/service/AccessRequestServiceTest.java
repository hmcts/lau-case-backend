package uk.gov.hmcts.reform.laubackend.cases.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestAction;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.AccessRequestRepository;

import java.sql.Timestamp;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessRequestServiceTest {

    @Mock
    private AccessRequestRepository accessRequestRepository;

    @InjectMocks
    private AccessRequestService accessRequestService;

    @Test
    void shouldSaveAccessRequestLog() {

        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setRequestType("CHALLENGED");
        accessRequest.setUserId("user-id");
        accessRequest.setCaseRef("case-ref");
        accessRequest.setReason("reason");
        accessRequest.setAction("APPROVED");
        accessRequest.setTimeLimit(Timestamp.valueOf("2021-08-01 00:00:00.000"));
        accessRequest.setLogTimestamp(Timestamp.valueOf("2021-08-01 00:00:00.000"));

        when(accessRequestRepository.save(any(AccessRequest.class))).thenReturn(accessRequest);

        // given
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(AccessRequestType.CHALLENGED)
            .userId("user-id")
            .caseRef("case-ref")
            .reason("reason")
            .action(AccessRequestAction.APPROVED)
            .timeLimit("2021-08-01T00:00:00.000Z")
            .timestamp("2021-08-01T00:00:00.000Z")
            .build();

        // when
        AccessRequestLog savedAccessRequestLog = accessRequestService.save(accessRequestLog);

        // then
        assertThat(savedAccessRequestLog).isNotNull();
        assertThat(savedAccessRequestLog.getRequestType()).isEqualTo(AccessRequestType.CHALLENGED);
        assertThat(savedAccessRequestLog.getUserId()).isEqualTo("user-id");
        assertThat(savedAccessRequestLog.getCaseRef()).isEqualTo("case-ref");
        assertThat(savedAccessRequestLog.getReason()).isEqualTo("reason");
        assertThat(savedAccessRequestLog.getAction()).isEqualTo(AccessRequestAction.APPROVED);
        assertThat(savedAccessRequestLog.getTimeLimit()).isEqualTo("2021-08-01T00:00:00.000Z");
        assertThat(savedAccessRequestLog.getTimestamp()).isEqualTo("2021-08-01T00:00:00.000Z");
    }

    @Test
    void shouldThrowExceptionWhenTimestampIsUnparsable() {
        // given
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(AccessRequestType.CHALLENGED)
            .userId("user-id")
            .caseRef("case-ref")
            .reason("reason")
            .action(AccessRequestAction.APPROVED)
            .timestamp("2021-08-01") // missing time part in timestamp
            .build();

        // when
        Throwable thrown = assertThrows(
            DateTimeParseException.class,
            () -> accessRequestService.save(accessRequestLog),
            "Expected save() to throw DateTimeParseException, but it didn't"
        );
        assertThat(thrown).hasMessageContaining("Text '2021-08-01' could not be parsed at index 10");
    }

    @Test
    void shouldDeleteCaseSearchId() {
        accessRequestService.deleteAccessRequestRecord("1", "2");
        verify(accessRequestRepository, times(1)).deleteAccessRequestByUserIdAndCaseRef("1", "2");
    }

}
