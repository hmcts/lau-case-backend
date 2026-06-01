package uk.gov.hmcts.reform.laubackend.cases.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import uk.gov.hmcts.reform.laubackend.cases.authorization.RestApiPreInvokeInterceptor;
import uk.gov.hmcts.reform.laubackend.cases.config.RestApiPreInvokeConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestApiPreInvokeConfigurationTest {

    @Mock
    private RestApiPreInvokeInterceptor restApiPreInvokeInterceptor;
    @InjectMocks
    private RestApiPreInvokeConfiguration restApiPreInvokeConfiguration;

    @Test
    void shouldAddRestApiPreInvokeInterceptor() {
        final InterceptorRegistry interceptorRegistry = mock(InterceptorRegistry.class);

        when(interceptorRegistry.addInterceptor(any())).thenReturn(mock(InterceptorRegistration.class));

        restApiPreInvokeConfiguration.addInterceptors(interceptorRegistry);

        verify(interceptorRegistry, times(1)).addInterceptor(any());
    }
}
