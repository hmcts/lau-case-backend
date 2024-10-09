package uk.gov.hmcts.reform.laubackend.cases.repository.helpers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryBuilderTest {

    @Spy
    private TimestampUtil timestampUtil;

    @InjectMocks
    private QueryBuilder queryBuilder;

    @Test
    void shouldReturnSpecification() {
        final ActionInputParamsHolder actionInputParamsHolder = new ActionInputParamsHolder(
            "123",
            "345",
            "CAVEAT",
            "VIEW",
            random(71, "123456"),
            "2000-08-23T22:20:05.200",
            "2001-08-23T22:20:05.200",
            "1",
            "1"
        );

        final Specification<CaseActionAudit> caseActionAuditSpecification = queryBuilder
            .buildCaseActionRequest(actionInputParamsHolder);

        verify(timestampUtil, times(1)).getTimestampValue("2000-08-23T22:20:05.200");
        verify(timestampUtil, times(1)).getTimestampValue("2001-08-23T22:20:05.200");

        assertNotNull(caseActionAuditSpecification, "Should be not null");
    }
}
