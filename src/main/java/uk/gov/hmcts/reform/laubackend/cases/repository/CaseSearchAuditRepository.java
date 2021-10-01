package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

@SuppressWarnings({"PMD.UseObjectForClearerAPI"})
@Repository
public interface CaseSearchAuditRepository extends JpaRepository<CaseSearchAudit, Long> {

}
