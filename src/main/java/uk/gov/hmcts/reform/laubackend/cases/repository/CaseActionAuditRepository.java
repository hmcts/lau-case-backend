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
            + "WHERE ca.user_id = COALESCE(:userId, ca.user_id) "
            + "AND (:caseRef IS NULL OR ca.case_ref = :caseRef) "
            + "AND (:caseTypeId IS NULL OR ca.case_type_id = :caseTypeId) "
            + "AND ca.case_action = COALESCE(:caseAction, ca.case_action) "
            + "AND (:caseJurisdictionId IS NULL "
            +   "OR ca.case_jurisdiction_id = :caseJurisdictionId) "
            + "AND ca.log_timestamp >= :startTime "
            + "AND ca.log_timestamp <= :endTime ",
            countQuery = "SELECT count(*) FROM ( "
                    + "SELECT 1 FROM case_action_audit ca "
                    + "WHERE ca.user_id = COALESCE(:userId, ca.user_id) "
                    + "AND (:caseRef IS NULL OR ca.case_ref = :caseRef) "
                    + "AND (:caseTypeId IS NULL OR ca.case_type_id = :caseTypeId) "
                    + "AND ca.case_action = COALESCE(:caseAction, ca.case_action) "
                    + "AND (:caseJurisdictionId IS NULL "
                    +   "OR ca.case_jurisdiction_id = :caseJurisdictionId) "
                    + "AND ca.log_timestamp >= :startTime "
                    + "AND ca.log_timestamp <= :endTime "
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
