package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.sql.Timestamp;

@SuppressWarnings({"PMD.UseObjectForClearerAPI","PMD.UnnecessaryAnnotationValueElement"})
@Repository
public interface CaseSearchAuditRepository extends JpaRepository<CaseSearchAudit, Long> {

    @Query(value = "SELECT * FROM case_search_audit_new cs "
        + "WHERE (:userId IS NULL OR cs.user_id = :userId) "
        + "AND (:caseRef IS NULL OR cast(:caseRef as text) = ANY (cs.case_refs)) "
        + "AND (cast(cast(:startTime as text) as timestamp) IS NULL OR cs.log_timestamp >= :startTime) "
        + "AND (cast(cast(:endTime as text) as timestamp) IS NULL OR cs.log_timestamp <= :endTime) ORDER BY cs.log_timestamp",
        countQuery = "SELECT count(*) FROM case_search_audit_new cs "
            + "WHERE (:userId IS NULL OR cs.user_id = :userId) "
            + "AND (:caseRef IS NULL OR cast(:caseRef as text) = ANY (cs.case_refs)) "
            + "AND (cast(cast(:startTime as text) as timestamp) IS NULL OR cs.log_timestamp >= :startTime) "
            + "AND (cast(cast(:endTime as text) as timestamp) IS NULL OR cs.log_timestamp <= :endTime) ",
        nativeQuery = true)
    Page<CaseSearchAudit> findCaseSearch(final @Param("userId") String userId,
                                     final @Param("caseRef") String caseRef,
                                     final @Param("startTime") Timestamp startTime,
                                     final @Param("endTime") Timestamp endTime,
                                     final Pageable pageable);

SELECT * FROM case_search_audit_new cs WHERE ($1 IS NULL OR cs.user_id = $2) AND ($3 IS NULL OR cast($4 as text) = ANY (cs.case_refs)) AND (cast(cast($5 as text) as timestamp) IS NULL OR cs.log_timestamp >= $6) AND (cast(cast($7 as text) as timestamp) IS NULL OR cs.log_timestamp <= $8) ORDER BY cs.log_timestamp, cs.timestamp asc limit $9
}
