package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.sql.Timestamp;

@SuppressWarnings({"PMD.UseObjectForClearerAPI","PMD.UnnecessaryAnnotationValueElement",
    "PMD.AvoidDuplicateLiterals","PMD.FinalParameterInAbstractMethod"})
@Repository
public interface CaseSearchAuditRepository extends JpaRepository<CaseSearchAudit, Long> {

    @Query(value = "SELECT cs.id, cs.user_id, cs.log_timestamp, cs.case_refs FROM case_search_audit cs "
        + "WHERE cs.user_id = COALESCE(:userId, cs.user_id) "
        + "AND (:caseRef IS NULL "
        +   "OR case_refs @> ARRAY[coalesce(cast(:caseRef as bigint), 10000000000)]) "
        + "AND cs.log_timestamp >= :startTime "
        + "AND cs.log_timestamp <= :endTime ",
        countQuery = "SELECT count(*) FROM ( "
            + "SELECT 1 FROM case_search_audit cs "
            + "WHERE cs.user_id = COALESCE(:userId, cs.user_id) "
            + "AND (:caseRef IS NULL "
            +   "OR case_refs @> ARRAY[coalesce(cast(:caseRef as bigint), 10000000000)]) "
            + "AND cs.log_timestamp >= :startTime "
            + "AND cs.log_timestamp <= :endTime "
            + "limit 10000) cs",
        nativeQuery = true)
    Page<CaseSearchAudit> findCaseSearch(final @Param("userId") String userId,
                                         final @Param("caseRef") String caseRef,
                                         final @Param("startTime") Timestamp startTime,
                                         final @Param("endTime") Timestamp endTime,
                                         final Pageable pageable);

}
