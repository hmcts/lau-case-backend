package uk.gov.hmcts.reform.laubackend.cases.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.JurisdictionCaseTypePair;

import java.util.List;

@Repository
public interface CaseActionAuditRepository extends JpaRepository<CaseActionAudit, Long>,
        JpaSpecificationExecutor<CaseActionAudit> {

    @Query(
        value = "SELECT case_jurisdiction_id jurisdiction, case_type_id caseType FROM case_action_audit "
        + "GROUP BY jurisdiction, caseType", nativeQuery = true)
    List<JurisdictionCaseTypePair> getJurisdictionsCaseTypes();
}
