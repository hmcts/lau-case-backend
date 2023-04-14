package uk.gov.hmcts.reform.laubackend.cases.repository.helpers;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SqlCountStatementInterceptor implements StatementInspector {

    public static final long serialVersionUID = 432973322;
    private static final String COUNT_STATEMENT = "SELECT COUNT(*) AS col_0_0_ FROM (";
    private static final String COUNT_SEARCH_STRING = "SELECT COUNT(";
    private static final String LIMIT_CRITERIA = " LIMIT 10000) ca";
    private static final String COUNT_REGEX = "count\\(.*?\\)";

    @Override
    public String inspect(final String sql) {
        if (!isEmpty(sql) && isCountCaseAction(sql)) {
            final String updatedCountSqlStatement = sql.replaceAll(COUNT_REGEX, "1").concat(LIMIT_CRITERIA);
            return COUNT_STATEMENT.concat(updatedCountSqlStatement);
        }
        return sql;
    }

    private static boolean isCountCaseAction(final String sql) {
        return containsIgnoreCase(sql, COUNT_SEARCH_STRING) && containsIgnoreCase(sql, "case_action_audit");
    }
}
