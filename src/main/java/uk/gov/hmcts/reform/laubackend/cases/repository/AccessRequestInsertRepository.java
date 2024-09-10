package uk.gov.hmcts.reform.laubackend.cases.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;

@Repository
public class AccessRequestInsertRepository {

    private static final String INSERT_QUERY_WITH_ENCRYPTION = """
        INSERT INTO public.access_request_audit
            (request_type,user_id, case_ref, action, log_timestamp, request_start_timestamp, request_end_timestamp,
            reason)
        VALUES (:requestType, :userId, :caseRef, :action, :logTimestamp, :requestStartTimestamp, :requestEndTimestamp,
            encode(pgp_sym_encrypt(cast(:reason as text), cast(:encryptionKey as text)), 'base64'))
            RETURNING id;
        """;

    @PersistenceContext
    private final EntityManager entityManager;

    public AccessRequestInsertRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public AccessRequest saveAccessRequestAuditWithEncryption(
        AccessRequest accessRequestAudit,
        String securityDbBackendEncryptionKey) {
        Query insertQuery = entityManager.createNativeQuery(INSERT_QUERY_WITH_ENCRYPTION);

        insertQuery.setParameter("requestType", accessRequestAudit.getRequestType())
            .setParameter("userId", accessRequestAudit.getUserId())
            .setParameter("caseRef", accessRequestAudit.getCaseRef())
            .setParameter("action", accessRequestAudit.getAction())
            .setParameter("logTimestamp", accessRequestAudit.getTimestamp())
            .setParameter("requestStartTimestamp", accessRequestAudit.getRequestStart())
            .setParameter("requestEndTimestamp", accessRequestAudit.getRequestEnd())
            .setParameter("reason", accessRequestAudit.getReason())
            .setParameter("encryptionKey", securityDbBackendEncryptionKey);

        long generatedId = ((Number) insertQuery.getSingleResult()).longValue();
        accessRequestAudit.setId(generatedId);

        return accessRequestAudit;
    }
}
