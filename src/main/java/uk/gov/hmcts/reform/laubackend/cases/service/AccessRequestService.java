package uk.gov.hmcts.reform.laubackend.cases.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.AccessRequestRepository;
import uk.gov.hmcts.reform.laubackend.cases.repository.helpers.QueryBuilder;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestGetRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.AccessRequestGetResponse;

import java.util.LinkedList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final QueryBuilder queryBuilder;

    @Value("${default.page.size}")
    private Integer defaultPageSize;

    public AccessRequestLog save(final AccessRequestLog accessRequestLog) {
        AccessRequest saved = accessRequestRepository.save(accessRequestLog.toModel());
        return AccessRequestLog.modelToDto(saved);
    }

    public AccessRequestGetResponse getAccessRequestRecords(AccessRequestGetRequest queryParams) {
        Page<AccessRequest> records = accessRequestRepository.findAll(
            queryBuilder.buildAccessRequestQuerySpec(queryParams),
            getPage(queryParams.getSize(), queryParams.getPage())
            );

        List<AccessRequestLog> accessLogs = new LinkedList<>();
        records.getContent().forEach(accessRequest -> accessLogs.add(AccessRequestLog.modelToDto(accessRequest)));

        return AccessRequestGetResponse.builder()
            .accessLog(accessLogs)
            .startRecordNumber(records.getNumber() * records.getSize() + 1)
            .moreRecords(records.hasNext())
            .totalNumberOfRecords(records.getTotalElements())
            .build();
    }

    @Transactional
    public void deleteAccessRequestRecord(String userId, String caseRef) {
        accessRequestRepository.deleteAccessRequestByUserIdAndCaseRef(userId, caseRef);
    }

    private Pageable getPage(final Integer size, final Integer page) {
        return PageRequest.of(
            page == null ? 0 : page - 1,
            size == null ? defaultPageSize : size,
            Sort.by(Sort.Direction.DESC, "timestamp")
        );
    }
}
