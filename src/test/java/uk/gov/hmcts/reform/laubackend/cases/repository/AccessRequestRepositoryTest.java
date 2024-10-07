package uk.gov.hmcts.reform.laubackend.cases.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestGetRequest;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.PageRequest.of;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=update",
    "spring.liquibase.enabled=false",
    "spring.flyway.enabled=true"
})

class AccessRequestRepositoryTest {
    private static final String ENCRYPTION_KEY = "ThisIsATestKeyForEncryption";

    private AccessRequestFindRepository accessRequestFindRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        final AccessRequestInsertRepository accessRequestInsertRepository =
            new AccessRequestInsertRepository(entityManager);
        // Insert 20 records
        for (int i = 1; i < 21; i++) {
            accessRequestInsertRepository
                .saveAccessRequestAuditWithEncryption(getAccessRequest(
                    String.valueOf(i),
                    String.valueOf(i),
                    valueOf(now().plusDays(i))
                ), ENCRYPTION_KEY);
        }
        accessRequestFindRepository = new AccessRequestFindRepository(entityManager, new TimestampUtil());
    }

    @Test
    void shouldReturnInDescendingOrderByTimestamp() {
        final Page<AccessRequest> accessRequests = accessRequestFindRepository
            .findAll(searchAccessRequest(), ENCRYPTION_KEY, getPage());
        List<AccessRequest> items = accessRequests.getContent();

        assertThat(items)
            .as("Results are not sorted in descending order")
            .extracting("timestamp", Timestamp.class)
            .isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    void shouldSearchByUserId() {
        AccessRequestGetRequest accessRequest = searchAccessRequest();
        accessRequest.setUserId("1");
        final Page<AccessRequest> accessRequests = accessRequestFindRepository
            .findAll(accessRequest, ENCRYPTION_KEY, getPage());

        assertThat(accessRequests.getContent()).hasSize(1);
        assertResults(accessRequests.getContent(), 1);
    }

    @Test
    void shouldSearchByCaseRef() {
        AccessRequestGetRequest accessRequest = searchAccessRequest();
        accessRequest.setCaseRef("2");
        final Page<AccessRequest> accessRequests = accessRequestFindRepository
            .findAll(accessRequest, ENCRYPTION_KEY, getPage());

        assertThat(accessRequests.getContent()).hasSize(1);
        assertResults(accessRequests.getContent(), 2);
    }

    @Test
    void shouldSearchByRequestType() {
        AccessRequestGetRequest accessRequest = searchAccessRequest();
        accessRequest.setRequestType(AccessRequestType.CHALLENGED);
        final Page<AccessRequest> accessRequests = accessRequestFindRepository
            .findAll(accessRequest, ENCRYPTION_KEY, getPage());

        assertThat(accessRequests.getContent()).hasSize(20);
        assertResults(accessRequests.getContent(), 20);
    }

    @Test
    void shouldGetAllRecords() {
        final Page<AccessRequest> accessRequests = accessRequestFindRepository
            .findAll(searchAccessRequest(), ENCRYPTION_KEY, getPage());

        assertThat(accessRequests.getContent()).hasSize(20);
    }

    @Test
    void shouldFindPageableResults() {
        final Page<AccessRequest> accessRequests = accessRequestFindRepository.findAll(
            searchAccessRequest(), ENCRYPTION_KEY, of(1, 10)
        );

        assertThat(accessRequests.getTotalElements()).isEqualTo(20);
        assertThat(accessRequests.getContent()).hasSize(10);
    }

    private AccessRequest getAccessRequest(final String userId, final String caseRef, final Timestamp timeStamp) {
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setRequestType("CHALLENGED");
        accessRequest.setUserId(userId);
        accessRequest.setCaseRef(caseRef);
        accessRequest.setReason("reason");
        accessRequest.setAction("APPROVED");
        accessRequest.setRequestEnd(timeStamp);
        accessRequest.setTimestamp(timeStamp);
        return accessRequest;
    }

    private Pageable getPage() {
        return of(0, 100);
    }

    private AccessRequestGetRequest searchAccessRequest() {
        AccessRequestGetRequest accessRequest = new AccessRequestGetRequest();
        accessRequest.setStartTimestamp(now().toString());
        accessRequest.setEndTimestamp(now().plusDays(20).toString());
        return accessRequest;
    }

    private void assertResults(final List<AccessRequest> content, final int value) {
        final String stringValue = String.valueOf(value);
        assertThat(content.get(0).getUserId()).isEqualTo(stringValue);
        assertThat(content.get(0).getCaseRef()).isEqualTo(stringValue);
        assertThat(content.get(0).getRequestType()).isEqualTo("CHALLENGED");
        assertThat(content.get(0).getReason()).isEqualTo("reason");
        assertThat(content.get(0).getAction()).isEqualTo("APPROVED");
    }

}
