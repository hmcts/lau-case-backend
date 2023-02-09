package uk.gov.hmcts.reform.laubackend.cases.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

import java.sql.Timestamp;

@SuppressWarnings({"PMD.UseObjectForClearerAPI","PMD.AvoidDuplicateLiterals"})
@Repository
public interface CaseActionAuditRepository extends JpaRepository<CaseActionAudit, Long> {

    @Query(value = "SELECT ca.* FROM case_action_audit ca "
        + "WHERE (cast(:userId as text) IS NULL OR ca.user_id = cast(:userId as text)) "
        + "AND (cast(:caseRef as text) IS NULL OR ca.case_ref = cast(:caseRef as text)) "
        + "AND (cast(:caseTypeId as text) IS NULL OR ca.case_type_id = cast(:caseTypeId as text)) "
        + "AND (cast(:caseAction as text) IS NULL OR ca.case_action = cast(:caseAction as text)) "
        + "AND (cast(:caseJurisdictionId as text) IS NULL "
        +   "OR ca.case_jurisdiction_id = cast(:caseJurisdictionId as text)) "
        + "AND (cast(cast(:startTime as varchar) as timestamp) IS NULL "
        +   "OR ca.log_timestamp >= cast(cast(:startTime as varchar) as timestamp)) "
        + "AND (cast(cast(:endTime as varchar) as timestamp) IS NULL "
        +   "OR ca.log_timestamp <= cast(cast(:endTime as varchar) as timestamp))",
        countQuery = "SELECT count(*) FROM ( "
            + "SELECT 1 FROM case_action_audit ca "
            + "WHERE (cast(:userId as text) IS NULL OR ca.user_id = cast(:userId as text)) "
            + "AND (cast(:caseRef as text) IS NULL OR ca.case_ref = cast(:caseRef as text)) "
            + "AND (cast(:caseTypeId as text) IS NULL OR  ca.case_type_id = cast(:caseTypeId as text)) "
            + "AND (cast(:caseAction as text) IS NULL OR  ca.case_action = cast(:caseAction as text)) "
            + "AND (cast(:caseJurisdictionId as text) IS NULL "
            +   "OR ca.case_jurisdiction_id = cast(:caseJurisdictionId as text)) "
            + "AND (cast(cast(:startTime as varchar) as timestamp) IS NULL "
            +   "OR ca.log_timestamp >= cast(cast(:startTime as varchar) as timestamp)) "
            + "AND (cast(cast(:endTime as varchar) as timestamp) IS NULL "
            +   "OR ca.log_timestamp <= cast(cast(:endTime as varchar) as timestamp))"
            + "limit 10000) ca",
        nativeQuery = true)
    Page<CaseActionAudit> findCaseView(String userId,
                                       String caseRef,
                                       String caseTypeId,
                                       String caseAction,
                                       String caseJurisdictionId,
                                       Timestamp startTime,
                                       Timestamp endTime,
                                       Pageable pageable);
}
