package uk.gov.hmcts.reform.laubackend.cases.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationScriptExceptionTest {

    final String script = "My test Message";

    @Test
    void shouldGetMessage() {
        assertThat(new MigrationScriptException(script)).hasMessage("Found migration not yet applied My test Message");
    }

    @Test
    void shouldCreateInstanceOfRuntimeException() {
        assertThat(new MigrationScriptException(script)).isInstanceOf(RuntimeException.class);
    }
}
