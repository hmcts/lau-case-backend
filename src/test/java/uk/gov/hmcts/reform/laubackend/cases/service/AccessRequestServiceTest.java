package uk.gov.hmcts.reform.laubackend.cases.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestAction;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.AccessRequestFindRepository;
import uk.gov.hmcts.reform.laubackend.cases.repository.AccessRequestRepository;
import uk.gov.hmcts.reform.laubackend.cases.repository.helpers.QueryBuilder;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestGetRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.AccessRequestGetResponse;

import java.sql.Timestamp;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

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

    @Mock
    private AccessRequestFindRepository accessRequestFindRepository;

    @Mock
    private QueryBuilder queryBuilder;

    @InjectMocks
    private AccessRequestService accessRequestService;

    @Test
    void shouldSaveAccessRequestLog() {

        AccessRequest accessRequest = getAccessRequest(false);
        accessRequest.setRequestType("CHALLENGED");
        accessRequest.setUserId("user-id");
        accessRequest.setCaseRef("case-ref");
        accessRequest.setReason("reason");
        accessRequest.setAction("APPROVED");
        accessRequest.setRequestEnd(Timestamp.valueOf("2021-08-01 23:59:59.999"));
        accessRequest.setTimestamp(Timestamp.valueOf("2021-08-01 00:00:00.000"));

        when(accessRequestRepository.save(any(AccessRequest.class))).thenReturn(accessRequest);

        // given
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(AccessRequestType.CHALLENGED)
            .userId("user-id")
            .caseRef("case-ref")
            .reason("reason")
            .action(AccessRequestAction.APPROVED)
            .requestEnd("2021-08-01T23:59:59.999Z")
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
        assertThat(savedAccessRequestLog.getRequestEnd()).isEqualTo("2021-08-01T23:59:59.999Z");
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
            .requestEnd("2021-08-01T23:59:59.999Z")
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
    void shouldRetrieveAccessRequestRecords() {
        // Given
        AccessRequestGetRequest queryParams = getAccessRequestGetRequest();
        queryParams.setSize(10);
        queryParams.setPage(1);

        AccessRequest accessRequest = getAccessRequest(false);
        List<AccessRequest> accessRequestList = Collections.singletonList(accessRequest);
        Page<AccessRequest> accessRequestPage = new PageImpl<>(
            accessRequestList,
            PageRequest.of(0, 10, Sort.by("timestamp").descending()),
            1
        );

        when(accessRequestFindRepository
                 .findAll(queryParams, null,
                          PageRequest.of(0, 10, Sort.by("timestamp").descending())
                 ))
            .thenReturn(accessRequestPage);

        // When
        AccessRequestGetResponse response = accessRequestService.getAccessRequestRecords(queryParams);

        // Then
        verify(accessRequestFindRepository, times(1))
            .findAll(queryParams, null,
                     PageRequest.of(0, 10, Sort.by("timestamp").descending())
            );

        assertThat(response).isNotNull();
        assertThat(response.getAccessLog()).hasSize(1);
        assertThat(response.getAccessLog().get(0)).isInstanceOf(AccessRequestLog.class);
        assertThat(response.getStartRecordNumber()).isEqualTo(1);
        assertThat(response.isMoreRecords()).isFalse();
        assertThat(response.getTotalNumberOfRecords()).isEqualTo(1);


        AccessRequestLog accessLog = response.getAccessLog().getFirst();
        assertThat(accessLog.getRequestType()).isEqualTo(AccessRequestType.CHALLENGED);
        assertThat(accessLog.getUserId()).isEqualTo("user-id");
        assertThat(accessLog.getCaseRef()).isEqualTo("case-ref");
        assertThat(accessLog.getReason()).isEqualTo("reason");
        assertThat(accessLog.getAction()).isEqualTo(AccessRequestAction.APPROVED);
        assertThat(accessLog.getRequestEnd()).isEqualTo("2021-08-01T23:59:59.999Z");
        assertThat(accessLog.getTimestamp()).isEqualTo("2021-08-01T00:00:00.000Z");
    }

    @Test
    void shouldSaveAutoApprovedAccessRequestLog() {
        AccessRequest accessRequest = getAccessRequest(true);

        when(accessRequestRepository.save(any(AccessRequest.class))).thenReturn(accessRequest);

        // given
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(AccessRequestType.CHALLENGED)
            .userId("user-id")
            .caseRef("case-ref")
            .reason("reason")
            .action(AccessRequestAction.AUTO_APPROVED)
            .requestEnd("2021-08-01T23:59:59.999Z")
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
        assertThat(savedAccessRequestLog.getAction()).isEqualTo(AccessRequestAction.AUTO_APPROVED);
        assertThat(savedAccessRequestLog.getRequestEnd()).isEqualTo("2021-08-01T23:59:59.999Z");
        assertThat(savedAccessRequestLog.getTimestamp()).isEqualTo("2021-08-01T00:00:00.000Z");
    }

    private AccessRequest getAccessRequest(final boolean isAutoApproved) {
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setRequestType("CHALLENGED");
        accessRequest.setUserId("user-id");
        accessRequest.setCaseRef("case-ref");
        accessRequest.setReason("reason");
        accessRequest.setAction(isAutoApproved ? "AUTO-APPROVED" : "APPROVED");
        accessRequest.setRequestEnd(Timestamp.valueOf("2021-08-01 23:59:59.999"));
        accessRequest.setTimestamp(Timestamp.valueOf("2021-08-01 00:00:00.000"));
        return accessRequest;
    }

    private AccessRequestGetRequest getAccessRequestGetRequest() {
        AccessRequestGetRequest accessRequest = new AccessRequestGetRequest();
        accessRequest.setRequestType(AccessRequestType.CHALLENGED);
        accessRequest.setUserId("user-id");
        accessRequest.setCaseRef("case-ref");
        accessRequest.setStartTimestamp("2021-08-01 23:59:59.999");
        accessRequest.setEndTimestamp("2021-08-01 00:00:00.000");
        return accessRequest;
    }
}
