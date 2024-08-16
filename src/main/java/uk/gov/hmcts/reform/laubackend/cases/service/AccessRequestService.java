package uk.gov.hmcts.reform.laubackend.cases.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.AccessRequestRepository;

@Service
@RequiredArgsConstructor
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;

    public AccessRequestLog save(final AccessRequestLog accessRequestLog) {
        AccessRequest saved = accessRequestRepository.save(accessRequestLog.toModel());
        return AccessRequestLog.modelToDto(saved);
    }

    @Transactional
    public void deleteAccessRequestRecord(String userId, String caseRef) {
        accessRequestRepository.deleteAccessRequestByUserIdAndCaseRef(userId, caseRef);
    }
}
