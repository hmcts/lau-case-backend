package uk.gov.hmcts.reform.laubackend.cases.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import uk.gov.hmcts.reform.laubackend.cases.utils.FlywayNoOpStrategy;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlywayConfigurationTest {

    private final FlywayMigrationStrategyConfiguration flywayMigrationStrategyConfiguration =
        new FlywayMigrationStrategyConfiguration();


    @Test
    void shouldReturnInstanceOfFlywayMigrationStrategy() {
        assertThat(flywayMigrationStrategyConfiguration.flywayMigrationStrategy())
            .isInstanceOf(FlywayMigrationStrategy.class);
        assertThat(flywayMigrationStrategyConfiguration.flywayMigrationStrategy())
            .isInstanceOf(FlywayNoOpStrategy.class);
    }

    @Test
    void shouldReturnNullFlyWayMigrationStrategy() {
        assertThat(flywayMigrationStrategyConfiguration.flywayVoidMigrationStrategy()).isNull();
    }
}
