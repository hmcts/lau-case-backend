package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.sql.Timestamp;

@SuppressWarnings({"PMD.UseObjectForClearerAPI","PMD.UnnecessaryAnnotationValueElement","PMD.AvoidDuplicateLiterals"})
@Repository
public interface CaseSearchAuditRepository extends JpaRepository<CaseSearchAudit, Long> {

    @Query(value = "SELECT cs.id, cs.user_id, cs.log_timestamp, cs.case_refs FROM case_search_audit cs "
        + "WHERE (cast(:userId as text) IS NULL OR cs.user_id=cast(:userId as text)) "
        + "AND (cast(:caseRef as text) IS NULL "
        +   "OR case_refs @> ARRAY[coalesce(cast(cast(:caseRef as text) as bigint), 10000000000)]) "
        + "AND (cast(cast(:startTime as varchar) as timestamp) IS NULL "
        +   "OR cs.log_timestamp >= cast(cast(:startTime as varchar) as timestamp)) "
        + "AND (cast(cast(:endTime as varchar) as timestamp) IS NULL "
        +   "OR cs.log_timestamp <= cast(cast(:endTime as varchar) as timestamp)) ",
        countQuery = "SELECT count(*) FROM ( "
            + "SELECT 1 FROM case_search_audit cs "
            + "WHERE (cast(:userId as text) IS NULL OR cs.user_id=cast(:userId as text)) "
            + "AND (cast(:caseRef as text) IS NULL "
            +   "OR case_refs @> ARRAY[coalesce(cast(cast(:caseRef as text) as bigint), 10000000000)]) "
            + "AND (cast(cast(:startTime as varchar) as timestamp) IS NULL "
            +   "OR cs.log_timestamp >= cast(cast(:startTime as varchar) as timestamp)) "
            + "AND (cast(cast(:endTime as varchar) as timestamp) IS NULL "
            +   "OR cs.log_timestamp <= cast(cast(:endTime as varchar) as timestamp)) "
            + "limit 100000) cs",
        nativeQuery = true)
    Page<CaseSearchAudit> findCaseSearch(final @Param("userId") String userId,
                                                   final @Param("caseRef") String caseRef,
                                                   final @Param("startTime") Timestamp startTime,
                                                   final @Param("endTime") Timestamp endTime,
                                                   final Pageable pageable);

    @Query(value = "SELECT cs.id, cs.user_id, cs.log_timestamp, cs.case_refs FROM case_search_audit cs "
        + "WHERE (cast(:userId as text) IS NULL OR cs.user_id=cast(:userId as text)) "
        + "AND (cast(:caseRef as text) IS NULL "
        +   "OR ARRAY_CONTAINS(cs.case_refs, coalesce(cast(cast(:caseRef as varchar) as bigint), 10000000000))) "
        + "AND (cast(cast(:startTime as varchar) as timestamp) IS NULL "
        +   "OR cs.log_timestamp >= cast(cast(:startTime as varchar) as timestamp)) "
        + "AND (cast(cast(:endTime as varchar) as timestamp) IS NULL "
        +   "OR cs.log_timestamp <= cast(cast(:endTime as varchar) as timestamp)) ",
        countQuery = "SELECT count(*) FROM ( "
            + "SELECT 1 FROM case_search_audit cs "
            + "WHERE (cast(:userId as text) IS NULL OR cs.user_id=cast(:userId as text)) "
            + "AND (cast(:caseRef as text) IS NULL "
            +   "OR ARRAY_CONTAINS(cs.case_refs, coalesce(cast(cast(:caseRef as varchar) as bigint), 10000000000))) "
            + "AND (cast(cast(:startTime as varchar) as timestamp) IS NULL "
            +   "OR cs.log_timestamp >= cast(cast(:startTime as varchar) as timestamp)) "
            + "AND (cast(cast(:endTime as varchar) as timestamp) IS NULL "
            +   "OR cs.log_timestamp <= cast(cast(:endTime as varchar) as timestamp)) "
            + "limit 100000) cs",
        nativeQuery = true)
    Page<CaseSearchAudit> findCaseSearchH2(final @Param("userId") String userId,
                                         final @Param("caseRef") String caseRef,
                                         final @Param("startTime") Timestamp startTime,
                                         final @Param("endTime") Timestamp endTime,
                                         final Pageable pageable);
}
