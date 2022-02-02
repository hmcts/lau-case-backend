package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.sql.Timestamp;

@SuppressWarnings({"PMD.UseObjectForClearerAPI","PMD.UnnecessaryAnnotationValueElement"})
@Repository
public interface CaseSearchAuditRepository extends JpaRepository<CaseSearchAudit, Long> {

    @EntityGraph(value = "CaseSearch.List")
    @Query("SELECT distinct cs FROM case_search_audit cs "
        + "LEFT JOIN FETCH case_search_audit_cases csa on csa.caseSearchAudit = cs.id "
        + "WHERE (:userId IS NULL OR cs.userId = :userId) "
        + "AND (:caseRef IS NULL OR csa.caseRef = :caseRef) "
        + "AND (cast(:startTime as timestamp) IS NULL OR cs.timestamp >= :startTime) "
        + "AND (cast(:endTime as timestamp) IS NULL OR cs.timestamp <= :endTime)")
    Page<CaseSearchAudit> findCaseSearch(final @Param("userId") String userId,
                                     final @Param("caseRef") String caseRef,
                                     final @Param("startTime") Timestamp startTime,
                                     final @Param("endTime") Timestamp endTime,
                                     final Pageable pageable);
}
