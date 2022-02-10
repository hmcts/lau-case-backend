package uk.gov.hmcts.reform.laubackend.cases.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

import java.sql.Timestamp;

@SuppressWarnings({"PMD.UseObjectForClearerAPI"})
@Repository
public interface CaseActionAuditRepository extends JpaRepository<CaseActionAudit, Long> {

    @Query(value = "SELECT * FROM case_action_audit ca "
            + "WHERE (cast(:userId as text) IS NULL OR ca.user_id=cast(:userId as text)) "
            + "AND (cast(:caseRef as text) IS NULL OR ca.case_ref = cast(:caseRef as text)) "
            + "AND (cast(:caseTypeId as text) IS NULL OR  ca.case_type_id=cast(:caseTypeId as text)) "
            + "AND (cast(:caseJurisdictionId as text) IS NULL "
            + "OR ca.case_jurisdiction_id=cast(:caseJurisdictionId as text)) "
            + "AND (cast(cast(:startTime as varchar) as timestamp) IS NULL "
            + "OR ca.log_timestamp >= cast(cast(:startTime as varchar) as timestamp)) "
            + "AND (cast(cast(:endTime as varchar) as timestamp) IS NULL "
            + "OR ca.log_timestamp <= cast(cast(:endTime as varchar) as timestamp)) ",
            nativeQuery = true)
    Page<CaseActionAudit> findCaseView(final @Param("userId") String userId,
                                       final @Param("caseRef") String caseRef,
                                       final @Param("caseTypeId") String caseTypeId,
                                       final @Param("caseJurisdictionId") String caseJurisdictionId,
                                       final @Param("startTime") Timestamp startTime,
                                       final @Param("endTime") Timestamp endTime,
                                       final Pageable pageable);
}
