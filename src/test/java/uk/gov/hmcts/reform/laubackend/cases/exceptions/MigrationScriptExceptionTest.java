package uk.gov.hmcts.reform.laubackend.cases.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationScriptExceptionTest {

    private static final String SCRIPT_MESSAGE = "My test Message";

    @Test
    void shouldGetMessage() {
        assertThat(new MigrationScriptException(SCRIPT_MESSAGE)).hasMessage(
            "Found migration not yet applied My test Message");
    }

    @Test
    void shouldCreateInstanceOfRuntimeException() {
        assertThat(new MigrationScriptException(SCRIPT_MESSAGE)).isInstanceOf(RuntimeException.class);
    }
}
