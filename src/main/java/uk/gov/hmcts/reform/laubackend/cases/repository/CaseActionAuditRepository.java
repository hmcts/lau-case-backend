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

    @Query(value = "SELECT ca.* FROM case_action_audit ca "
        + "WHERE ca.user_id = COALESCE(cast(:userId as text), ca.user_id) "
        + "AND (cast(:caseRef as text) IS NULL OR ca.case_ref = cast(:caseRef as text)) "
        + "AND (cast(:caseTypeId as text) IS NULL OR ca.case_type_id = cast(:caseTypeId as text)) "
        + "AND ca.case_action = COALESCE(cast(:caseAction as text), ca.case_action) "
        + "AND (cast(:caseJurisdictionId as text) IS NULL "
        +   "OR ca.case_jurisdiction_id = cast(:caseJurisdictionId as text)) "
        + "AND ca.log_timestamp >= COALESCE(cast(:startTime as timestamp), ca.log_timestamp) "
        + "AND ca.log_timestamp <= COALESCE(cast(:endTime as timestamp), ca.log_timestamp) ",
        countQuery = "SELECT count(*) FROM ( "
            + "SELECT 1 FROM case_action_audit ca "
            + "WHERE ca.user_id = COALESCE(cast(:userId as text), ca.user_id) "
            + "AND (cast(:caseRef as text) IS NULL OR ca.case_ref = cast(:caseRef as text)) "
            + "AND (cast(:caseTypeId as text) IS NULL OR ca.case_type_id = cast(:caseTypeId as text)) "
            + "AND ca.case_action = COALESCE(cast(:caseAction as text), ca.case_action) "
            + "AND (cast(:caseJurisdictionId as text) IS NULL "
            +   "OR ca.case_jurisdiction_id = cast(:caseJurisdictionId as text)) "
            + "AND ca.log_timestamp >= COALESCE(cast(:startTime as timestamp), ca.log_timestamp) "
            + "AND ca.log_timestamp <= COALESCE(cast(:endTime as timestamp), ca.log_timestamp) "
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
