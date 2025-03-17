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
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestGetRequest;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@SuppressWarnings({"unchecked", "PMD.LawOfDemeter"})
@Repository
public class AccessRequestFindRepository {

    private static final String SELECT_CRITERIA = """
        SELECT ara.id, ara.request_type, ara.user_id, ara.case_ref, ara.action, ara.log_timestamp,
            ara.request_end_timestamp,
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

    private final TimestampUtil timestampUtil;

    @Autowired
    public AccessRequestFindRepository(final EntityManager entityManager, final TimestampUtil timestampUtil) {
        this.timestampUtil = timestampUtil;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Page<AccessRequest> findAll(final AccessRequestGetRequest accessRequestGetRequest,
                                                 final String encryptionKey,
                                                 final Pageable pageable) {
        final List<String> queryParts = new LinkedList<>();

        queryParts.add(SELECT_CRITERIA);
        queryParts.add(FROM);

        addSearchCriteria(queryParts, accessRequestGetRequest);

        queryParts.add(ORDER);

        final String queryString = String.join(" ", queryParts);
        final Query query = entityManager.createNativeQuery(queryString, AccessRequest.class);
        setSearchParams(query, accessRequestGetRequest);
        query.setParameter("encryptionKey", encryptionKey);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        final List<AccessRequest> results = query.getResultList();
        long totalCount = countResults(accessRequestGetRequest);

        return new PageImpl<>(results, pageable, totalCount);
    }

    private long countResults(final AccessRequestGetRequest accessRequestGetRequest) {

        final List<String> queryParts = new LinkedList<>();
        queryParts.add("SELECT count(*) FROM (");
        queryParts.add("SELECT 1");
        queryParts.add(FROM);

        addSearchCriteria(queryParts, accessRequestGetRequest);

        queryParts.add("limit 10000) ara");
        final String queryString = String.join(" ", queryParts);
        final Query countQuery = entityManager.createNativeQuery(queryString);
        setSearchParams(countQuery, accessRequestGetRequest);
        return ((Number) countQuery.getSingleResult()).intValue();
    }

    private void addSearchCriteria(List<String> queryParts,
                                   AccessRequestGetRequest accessRequestGetRequest) {
        List<String> searchParts = createSearchCriteria(accessRequestGetRequest);
        if (!searchParts.isEmpty()) {
            queryParts.add(WHERE);
            queryParts.add(String.join(" AND ", searchParts));
        }
    }


    private List<String> createSearchCriteria(final AccessRequestGetRequest accessRequestGetRequest) {
        final List<String> criteria = new LinkedList<>();
        if (!isEmpty(accessRequestGetRequest.getUserId())) {
            criteria.add(USER_ID_CRITERIA);
        }

        if (!isEmpty(accessRequestGetRequest.getCaseRef())) {
            criteria.add(CASE_REF_CRITERIA);
        }

        if (!isNull(accessRequestGetRequest.getRequestType())) {
            criteria.add(REQUEST_TYPE_CRITERIA);
        }

        criteria.add(TIMESTAMP_START_CRITERIA);
        criteria.add(TIMESTAMP_END_CRITERIA);

        return criteria;
    }

    private void setSearchParams(final Query query,
                                 final AccessRequestGetRequest accessRequestGetRequest) {

        query.setParameter("startTime",  timestampUtil.getTimestampValue(accessRequestGetRequest.getStartTimestamp()));
        query.setParameter("endTime", timestampUtil.getTimestampValue(accessRequestGetRequest.getEndTimestamp()));

        if (!isEmpty(accessRequestGetRequest.getUserId())) {
            query.setParameter("userId", accessRequestGetRequest.getUserId());
        }
        if (!isEmpty(accessRequestGetRequest.getCaseRef())) {
            query.setParameter("caseRef", accessRequestGetRequest.getCaseRef());
        }
        if (!isNull(accessRequestGetRequest.getRequestType())) {
            query.setParameter("requestType", accessRequestGetRequest.getRequestType().name());
        }
    }
}
