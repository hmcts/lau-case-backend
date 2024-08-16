package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;

@Repository
public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {
    void deleteAccessRequestByUserIdAndCaseRef(String userId, String caseRef);
}
