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

    @Query("SELECT ca FROM case_action_audit ca "
            + "WHERE (:userId IS NULL OR ca.userId = :userId) "
            + "AND (:caseRef IS NULL OR ca.caseRef = :caseRef) "
            + "AND (:caseTypeId IS NULL OR ca.caseTypeId = :caseTypeId) "
            + "AND (:caseJurisdictionId IS NULL OR ca.caseJurisdictionId = :caseJurisdictionId) "
            + "AND (cast(:startTime as timestamp) IS NULL OR ca.timestamp >= :startTime) "
            + "AND (cast(:endTime as timestamp) IS NULL OR ca.timestamp <= :endTime)")
    Page<CaseActionAudit> findCaseView(final @Param("userId") String userId,
                                       final @Param("caseRef") String caseRef,
                                       final @Param("caseTypeId") String caseTypeId,
                                       final @Param("caseJurisdictionId") String caseJurisdictionId,
                                       final @Param("startTime") Timestamp startTime,
                                       final @Param("endTime") Timestamp endTime,
                                       final Pageable pageable);
}
