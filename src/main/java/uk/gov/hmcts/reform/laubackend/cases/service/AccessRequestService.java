package uk.gov.hmcts.reform.laubackend.cases.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.AccessRequestFindRepository;
import uk.gov.hmcts.reform.laubackend.cases.repository.AccessRequestInsertRepository;
import uk.gov.hmcts.reform.laubackend.cases.repository.AccessRequestRepository;
import uk.gov.hmcts.reform.laubackend.cases.repository.helpers.QueryBuilder;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestGetRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.AccessRequestGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.util.LinkedList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final AccessRequestInsertRepository accessRequestInsertRepository;
    private final AccessRequestFindRepository accessRequestFindRepository;
    private final QueryBuilder queryBuilder;
    private final TimestampUtil timestampUtil;

    @Value("${default.page.size}")
    private Integer defaultPageSize;

    @Value("${security.db.backend-encryption-key}")
    private String securityDbBackendEncryptionKey;

    @Value("${security.db.encryption-enabled}")
    private Boolean encryptionEnabled;


    public AccessRequestLog save(final AccessRequestLog accessRequestLog) {
        final AccessRequest accessRequest;
        if (BooleanUtils.isTrue(encryptionEnabled)) {
            accessRequest = accessRequestInsertRepository
                .saveAccessRequestAuditWithEncryption(accessRequestLog.toModel(), securityDbBackendEncryptionKey);
        } else {
            accessRequest = accessRequestRepository.save(accessRequestLog.toModel());;
        }
        return AccessRequestLog.modelToDto(accessRequest);
    }

    public AccessRequestGetResponse getAccessRequestRecords(AccessRequestGetRequest queryParams) {
        Page<AccessRequest> records = accessRequestFindRepository.findAll(
            queryBuilder.buildAccessRequest(queryParams),
            securityDbBackendEncryptionKey,
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
