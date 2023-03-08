package uk.gov.hmcts.reform.laubackend.cases.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

import java.sql.Timestamp;

@SuppressWarnings({"PMD.UseObjectForClearerAPI","PMD.AvoidDuplicateLiterals","PMD.FinalParameterInAbstractMethod"})
@Repository
public interface CaseActionAuditRepository extends JpaRepository<CaseActionAudit, Long> {

    @Query(value = "SELECT * FROM case_action_audit ca "
            + "WHERE  ca.log_timestamp >= cast(cast(:startTime as varchar) as timestamp) "
            + "AND ca.log_timestamp <= cast(cast(:endTime as varchar) as timestamp)"
            +   "AND  (CASE "
            + "WHEN (cast(:userId as text) is not null) "
            + "THEN cast(ca.user_id as text) = cast(:userId as text) ELSE 1=1 END) "
            + "AND (CASE "
            + "WHEN (cast(:caseRef as text) is not null)  "
            + "THEN cast(ca.case_ref as text) = cast(:caseRef as text) ELSE 1=1 END) "
            + "AND (CASE "
            + "WHEN (cast(:caseTypeId as text) is not null ) "
            + "THEN cast(ca.case_type_id as text) = cast(:caseTypeId as text) ELSE 1=1 END) "
            + "AND (CASE "
            + "WHEN (cast(:caseAction as text) is not null ) "
            + "THEN cast(ca.case_action as text) = cast(:caseAction as text) ELSE 1=1 END) "
            + "AND (CASE "
            + "WHEN (cast(:caseJurisdictionId as text) is not null) "
            + "THEN cast(ca.case_jurisdiction_id as text) = cast(:caseJurisdictionId as text) ELSE 1=1 END) ",
            countQuery = "SELECT count(*) FROM ( "
                    + "SELECT 1 FROM case_action_audit ca "
                    + "WHERE  ca.log_timestamp >= cast(cast(:startTime as varchar) as timestamp) "
                    + "AND ca.log_timestamp <= cast(cast(:endTime as varchar) as timestamp) "
                    + "AND (CASE "
                    + "WHEN (cast(:userId as text) is not null) "
                    + "THEN cast(ca.user_id as text) = cast(:userId as text) ELSE 1=1 END) "
                    + "AND (CASE "
                    + "WHEN (cast(:caseRef as text) is not null)  "
                    + "THEN cast(ca.case_ref as text) = cast(:caseRef as text) ELSE 1=1 END) "
                    + "AND (CASE "
                    + "WHEN (cast(:caseTypeId as text) is not null ) "
                    + "THEN cast(ca.case_type_id as text) = cast(:caseTypeId as text) ELSE 1=1 END) "
                    + "AND (CASE "
                    + "WHEN (cast(:caseAction as text) is not null ) "
                    + "THEN cast(ca.case_action as text) = cast(:caseAction as text) ELSE 1=1 END) "
                    + "AND (CASE "
                    + "WHEN (cast(:caseJurisdictionId as text) is not null) "
                    + "THEN cast(ca.case_jurisdiction_id as text) = cast(:caseJurisdictionId as text) ELSE 1=1 END) "
                    + "limit 10000) ca",
            nativeQuery = true)
    Page<CaseActionAudit> findCaseView(final @Param("userId") String userId,
                                       final @Param("caseRef") String caseRef,
                                       final @Param("caseTypeId") String caseTypeId,
                                       final @Param("caseAction") String caseAction,
                                       final @Param("caseJurisdictionId") String caseJurisdictionId,
                                       final @Param("startTime") Timestamp startTime,
                                       final @Param("endTime") Timestamp endTime,
                                       final Pageable pageable);
}
