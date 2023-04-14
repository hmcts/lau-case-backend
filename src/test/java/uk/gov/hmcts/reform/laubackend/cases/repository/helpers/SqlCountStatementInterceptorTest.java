package uk.gov.hmcts.reform.laubackend.cases.repository.helpers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SqlCountStatementInterceptorTest {

    @Test
    void shouldRefactorCountQuery() {
        final SqlCountStatementInterceptor sqlCountStatementInterceptor = new SqlCountStatementInterceptor();
        final String modifiedCountQuery = sqlCountStatementInterceptor.inspect("select count(123) from "
                + "case_action_audit");

        assertThat(modifiedCountQuery).isEqualTo("SELECT COUNT(*) AS col_0_0_ FROM (select 1 from case_action_audit "
                + "LIMIT 10000) ca");
    }

    @Test
    void shouldNotRefactorCountQuery() {
        final SqlCountStatementInterceptor sqlCountStatementInterceptor = new SqlCountStatementInterceptor();
        final String modifiedCountQuery = sqlCountStatementInterceptor.inspect("select count(123) from "
                + "some_weird_table");

        assertThat(modifiedCountQuery).isEqualTo("select count(123) from "
                + "some_weird_table");
    }

    @Test
    void shouldNotRefactorCountQueryDueToCountMissing() {
        final SqlCountStatementInterceptor sqlCountStatementInterceptor = new SqlCountStatementInterceptor();
        final String modifiedCountQuery = sqlCountStatementInterceptor.inspect("select * from "
                + "case_action_audit");

        assertThat(modifiedCountQuery).isEqualTo("select * from "
                + "case_action_audit");
    }
}