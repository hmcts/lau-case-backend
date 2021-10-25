package uk.gov.hmcts.reform.laubackend.cases.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import uk.gov.hmcts.reform.laubackend.cases.authorization.AuthService;
import uk.gov.hmcts.reform.laubackend.cases.authorization.AuthorisedServices;
import uk.gov.hmcts.reform.laubackend.cases.authorization.RestApiPreInvokeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestApiPreInvokeInterceptorTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthorisedServices authorisedServices;

    @InjectMocks
    private RestApiPreInvokeInterceptor restApiPreInvokeInterceptor;

    @Test
    void shouldReturnTrueWhenTokenIsValid() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);
        final String header = "Super cool header";
        final String serviceName = "super_cool_service";

        when(httpServletRequest.getHeader("ServiceAuthorization")).thenReturn(header);
        when(authService.authenticate(header)).thenReturn(serviceName);
        when(authorisedServices.hasService(serviceName)).thenReturn(true);

        final boolean isValidRequest = restApiPreInvokeInterceptor
                .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(isValidRequest).isEqualTo(true);
    }

    @Test
    void shouldThrowUnauthorisedExceptionWhenRequestInvalid() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);
        final String header = "Super cool header";
        final String serviceName = "super_cool_service";


        when(httpServletRequest.getHeader("ServiceAuthorization")).thenReturn(header);
        when(authService.authenticate(header)).thenReturn(serviceName);
        when(authorisedServices.hasService(serviceName)).thenReturn(false);

        final boolean isValidRequest = restApiPreInvokeInterceptor
                .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(((MockHttpServletResponse) httpServletResponse).getContentAsString())
                .isEqualTo("Unable to authenticate service request.");
        assertThat(((MockHttpServletResponse) httpServletResponse).getStatus())
                .isEqualTo(SC_UNAUTHORIZED);
        assertThat(isValidRequest).isEqualTo(false);
    }

}