package uk.gov.hmcts.reform.laubackend.cases.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;

import java.sql.Timestamp;

@SuppressWarnings({"PMD.UseObjectForClearerAPI"})
@Repository
public interface CaseViewAuditRepository extends JpaRepository<CaseViewAudit, Long> {

    @Query("SELECT cv FROM case_view_audit cv "
            + "WHERE (:userId IS NULL OR cv.userId = :userId) "
            + "AND (:caseRef IS NULL OR cv.caseRef = :caseRef) "
            + "AND (:caseTypeId IS NULL OR cv.caseTypeId = :caseTypeId) "
            + "AND (:caseJurisdictionId IS NULL OR cv.caseJurisdictionId = :caseJurisdictionId) "
            + "AND (cast(:startTime as timestamp) IS NULL OR cv.timestamp >= :startTime) "
            + "AND (cast(:endTime as timestamp) IS NULL OR cv.timestamp <= :endTime)")
    Page<CaseViewAudit> findCaseView(final @Param("userId") String userId,
                                     final @Param("caseRef") String caseRef,
                                     final @Param("caseTypeId") String caseTypeId,
                                     final @Param("caseJurisdictionId") String caseJurisdictionId,
                                     final @Param("startTime") Timestamp startTime,
                                     final @Param("endTime") Timestamp endTime,
                                     final Pageable pageable);
}
