package uk.gov.hmcts.reform.laubackend.cases.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;

import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@SuppressWarnings("unchecked")
@Repository
public class AccessRequestFindRepository {

    private static final String SELECT_CRITERIA = """
        SELECT ara.id, ara.request_type, ara.user_id, ara.case_ref, ara.action, ara.log_timestamp,
            ara.request_start_timestamp, ara.request_end_timestamp,
            pgp_sym_decrypt(decode(ara.reason, 'base64'), cast(:encryptionKey as text)) as reason""";

    private static final String FROM = "FROM access_request_audit ara";
    private static final String WHERE = "WHERE";
    private static final String TIMESTAMP_START_CRITERIA = "ara.log_timestamp >= :startTime";
    private static final String TIMESTAMP_END_CRITERIA = "ara.log_timestamp <= :endTime";
    private static final String USER_ID_CRITERIA = "ara.user_id = :userId";
    private static final String CASE_REF_CRITERIA = "ara.case_ref = :caseRef";
    private static final String REQUEST_TYPE_CRITERIA = "ara.request_type = :requestType";
    private static final String ORDER = "ORDER BY log_timestamp DESC";

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public AccessRequestFindRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Page<AccessRequest> findAll(final AccessRequest accessRequest,
                                                 final String encryptionKey,
                                                 final Pageable pageable) {
        final List<String> queryParts = new LinkedList<>();

        queryParts.add(SELECT_CRITERIA);
        queryParts.add(FROM);

        addSearchCriteria(queryParts, accessRequest);

        queryParts.add(ORDER);

        final String queryString = String.join(" ", queryParts);
        final Query query = entityManager.createNativeQuery(queryString, AccessRequest.class);
        setSearchParams(query, accessRequest);
        query.setParameter("encryptionKey", encryptionKey);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        final List<AccessRequest> results = query.getResultList();
        long totalCount = countResults(encryptionKey, accessRequest);

        return new PageImpl<>(results, pageable, totalCount);
    }

    private long countResults(final String encryptionKey,
                              final AccessRequest accessRequest) {

        final List<String> queryParts = new LinkedList<>();
        queryParts.add("SELECT count(*) FROM (");
        queryParts.add("SELECT 1");
        queryParts.add(FROM);

        addSearchCriteria(queryParts, accessRequest);

        queryParts.add("limit 10000) ara");
        final String queryString = String.join(" ", queryParts);
        final Query countQuery = entityManager.createNativeQuery(queryString);
        setSearchParams(countQuery, accessRequest);
        return ((Number) countQuery.getSingleResult()).intValue();
    }

    private void addSearchCriteria(List<String> queryParts,
                                  AccessRequest accessRequest) {
        List<String> searchParts = createSearchCriteria(accessRequest);
        if (!searchParts.isEmpty()) {
            queryParts.add(WHERE);
            queryParts.add(String.join(" AND ", searchParts));
        }
    }


    private List<String> createSearchCriteria(final AccessRequest accessRequest) {
        final List<String> criteria = new LinkedList<>();
        if (!isEmpty(accessRequest.getUserId())) {
            criteria.add(USER_ID_CRITERIA);
        }

        if (!isEmpty(accessRequest.getCaseRef())) {
            criteria.add(CASE_REF_CRITERIA);
        }

        if (!isEmpty(accessRequest.getRequestType())) {
            criteria.add(REQUEST_TYPE_CRITERIA);
        }

        if (accessRequest.getRequestStart() != null) {
            criteria.add(TIMESTAMP_START_CRITERIA);
        }

        if (accessRequest.getRequestEnd() != null) {
            criteria.add(TIMESTAMP_END_CRITERIA);
        }
        return criteria;
    }

    private void setSearchParams(final Query query,
                                 final AccessRequest accessRequest) {
        if (accessRequest.getRequestStart() != null) {
            query.setParameter("startTime", accessRequest.getRequestStart());
        }

        if (accessRequest.getRequestEnd() != null) {
            query.setParameter("endTime", accessRequest.getRequestEnd());
        }

        if (!isEmpty(accessRequest.getUserId())) {
            query.setParameter("userId", accessRequest.getUserId());
        }
        if (!isEmpty(accessRequest.getCaseRef())) {
            query.setParameter("caseRef", accessRequest.getCaseRef());
        }
        if (!isEmpty(accessRequest.getRequestType())) {
            query.setParameter("requestType", accessRequest.getRequestType());
        }
    }
}
