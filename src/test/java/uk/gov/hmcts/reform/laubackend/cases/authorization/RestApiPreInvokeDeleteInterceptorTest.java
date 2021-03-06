package uk.gov.hmcts.reform.laubackend.cases.authorization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestApiPreInvokeDeleteInterceptorTest {

    final RestApiPreInvokeDeleteInterceptor restApiPreInvokeDeleteInterceptor =
            new RestApiPreInvokeDeleteInterceptor();

    @Test
    void shouldAllowToDeleteRecord() {
        setField(restApiPreInvokeDeleteInterceptor, "allowDeleteRecord", "true");
        final boolean isRecordDeleteAllowed = restApiPreInvokeDeleteInterceptor
                .preHandle(mock(HttpServletRequest.class),
                mock(HttpServletResponse.class),
                mock(Object.class));

        assertThat(isRecordDeleteAllowed).isEqualTo(true);
    }

    @Test
    void shouldNotAllowToDeleteRecord() {
        setField(restApiPreInvokeDeleteInterceptor, "allowDeleteRecord", "false");

        final boolean isRecordDeleteAllowed = restApiPreInvokeDeleteInterceptor
                .preHandle(mock(HttpServletRequest.class),
                mock(HttpServletResponse.class),
                mock(Object.class));

        assertThat(isRecordDeleteAllowed).isEqualTo(false);
    }
}