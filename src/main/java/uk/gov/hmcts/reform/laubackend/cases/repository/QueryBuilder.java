package uk.gov.hmcts.reform.laubackend.cases.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.springframework.data.domain.PageRequest.of;

@Service
public class QueryBuilder {

    @Autowired
    private TimestampUtil timestampUtil;

    public Specification<CaseActionAudit> buildCaseActionRequest(final ActionInputParamsHolder inputParamsHolder) {
        final CaseActionAudit caseActionAudit = createCaseActionAudit(inputParamsHolder);

        return getSpecFromDatesAndExample(
                timestampUtil.getTimestampValue(inputParamsHolder.getStartTime()),
                timestampUtil.getTimestampValue(inputParamsHolder.getEndTime()),
                Example.of(caseActionAudit, getExampleMatcher()));
    }

    public Specification<CaseActionAudit> getSpecFromDatesAndExample(final Timestamp startTime,
                                                                     final Timestamp endTime,
                                                                     final Example<CaseActionAudit> example) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (startTime != null) {
                predicates.add(builder.greaterThan(root.get("timestamp"), startTime));
            }
            if (endTime != null) {
                predicates.add(builder.lessThan(root.get("timestamp"), endTime));
            }
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    private ExampleMatcher getExampleMatcher() {
        return ExampleMatcher.matching()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withIgnoreNullValues();
    }

    private CaseActionAudit createCaseActionAudit(final ActionInputParamsHolder inputParamsHolder) {
        return new CaseActionAudit(
                inputParamsHolder.getUserId(),
                inputParamsHolder.getCaseRef(),
                upperCase(inputParamsHolder.getCaseAction()),
                upperCase(inputParamsHolder.getCaseJurisdictionId()),
                upperCase(inputParamsHolder.getCaseTypeId()),
                timestampUtil.getTimestampValue(inputParamsHolder.getEndTime())
        );
    }
}
