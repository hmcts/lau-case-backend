package uk.gov.hmcts.reform.laubackend.cases.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

@Repository
public interface CaseActionAuditRepository extends JpaRepository<CaseActionAudit, Long>,
        JpaSpecificationExecutor<CaseActionAudit> {
}
