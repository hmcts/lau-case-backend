package uk.gov.hmcts.reform.laubackend.cases.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InvalidRequestExceptionTest {

    @Test
    void shouldCreateInstanceOfException() {
        final InvalidRequestException invalidRequestException = new InvalidRequestException("error", BAD_REQUEST);
        assertThat(invalidRequestException).isInstanceOf(Exception.class);
    }

    @Test
    void shouldCreateInvalidRequestException() {
        final InvalidRequestException invalidRequestException = new InvalidRequestException("error", BAD_REQUEST);
        assertThat(invalidRequestException.getMessage()).isEqualTo("error");
        assertThat(invalidRequestException.getErrorCode()).isEqualTo(BAD_REQUEST);
    }
}
