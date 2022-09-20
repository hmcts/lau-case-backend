package uk.gov.hmcts.reform.laubackend.cases.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import uk.gov.hmcts.reform.laubackend.cases.authorization.AuthorizationAuthenticator;
import uk.gov.hmcts.reform.laubackend.cases.authorization.RestApiPreInvokeInterceptor;
import uk.gov.hmcts.reform.laubackend.cases.authorization.ServiceAuthorizationAuthenticator;
import uk.gov.hmcts.reform.laubackend.cases.constants.CaseAction;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidAuthorizationException;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.CASE_ACTION;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_AUDIT_INVESTIGATOR_ROLE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_SERVICE_LOGS_ROLE;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SuppressWarnings({"PMD.LawOfDemeter"})
class RestApiPreInvokeInterceptorTest {

    @Mock
    private ServiceAuthorizationAuthenticator serviceAuthorizationAuthenticator;

    @Mock
    private AuthorizationAuthenticator authorizationAuthenticator;

    @InjectMocks
    private RestApiPreInvokeInterceptor restApiPreInvokeInterceptor;

    @Test
    void shouldReturnTrueWhenServiceAndAuthTokenIsValidForPostRequest() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);

        doNothing().when(serviceAuthorizationAuthenticator).authorizeServiceToken(httpServletRequest);

        when(httpServletRequest.getMethod()).thenReturn(POST.name());

        final boolean isValidRequest = restApiPreInvokeInterceptor
                .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(isValidRequest).isTrue();
    }

    @Test
    void shouldReturnTrueWhenServiceAndAuthTokenIsValidForGetAuditInvestigatorRequest() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);
        final List<String> roles = Collections.singletonList(AUTHORISATION_AUDIT_INVESTIGATOR_ROLE);

        when(httpServletRequest.getMethod()).thenReturn(GET.name());
        when(authorizationAuthenticator.authorizeAuthorizationToken(httpServletRequest)).thenReturn(roles);

        final boolean isValidRequest = restApiPreInvokeInterceptor
            .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(isValidRequest).isTrue();
    }

    @Test
    void shouldReturnTrueWhenServiceAndAuthTokenIsValidForGetServiceLogsRequest() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);
        final List<String> roles = Collections.singletonList(AUTHORISATION_SERVICE_LOGS_ROLE);

        when(httpServletRequest.getMethod()).thenReturn(GET.name());
        when(httpServletRequest.getParameter(CASE_ACTION)).thenReturn(CaseAction.DELETE.name());
        when(authorizationAuthenticator.authorizeAuthorizationToken(httpServletRequest)).thenReturn(roles);

        final boolean isValidRequest = restApiPreInvokeInterceptor
            .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(isValidRequest).isTrue();
    }

    @Test
    void shouldReturnTrueWhenServiceAndAuthTokenIsValidForMultipleRoleRequest() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);
        List<String> roles = Arrays.asList(AUTHORISATION_SERVICE_LOGS_ROLE, AUTHORISATION_AUDIT_INVESTIGATOR_ROLE);

        when(httpServletRequest.getMethod()).thenReturn(GET.name());
        when(authorizationAuthenticator.authorizeAuthorizationToken(httpServletRequest)).thenReturn(roles);

        final boolean isValidRequest = restApiPreInvokeInterceptor
            .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(isValidRequest).isTrue();
    }

    @Test
    void shouldThrowInvalidServiceAuthorizationExceptionWhenServiceNameInvalid() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);

        doThrow(new InvalidServiceAuthorizationException("Yabba Dabba Doo"))
                .when(serviceAuthorizationAuthenticator)
                .authorizeServiceToken(httpServletRequest);

        final boolean isValidRequest = restApiPreInvokeInterceptor
                .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(((MockHttpServletResponse) httpServletResponse).getErrorMessage())
                .isEqualTo("Yabba Dabba Doo");
        assertThat(((MockHttpServletResponse) httpServletResponse).getStatus())
                .isEqualTo(SC_FORBIDDEN);
        assertThat(isValidRequest).isFalse();
    }

    @Test
    void shouldThrowInvalidAuthorizationExceptionWhenMissingAuthHeader() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);

        doNothing().when(serviceAuthorizationAuthenticator).authorizeServiceToken(httpServletRequest);
        when(httpServletRequest.getMethod()).thenReturn(GET.name());
        doThrow(new InvalidAuthorizationException("Scooby Doo"))
                .when(authorizationAuthenticator)
                .authorizeAuthorizationToken(httpServletRequest);

        final boolean isValidRequest = restApiPreInvokeInterceptor
                .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(((MockHttpServletResponse) httpServletResponse).getErrorMessage())
                .isEqualTo("Scooby Doo");
        assertThat(((MockHttpServletResponse) httpServletResponse).getStatus())
                .isEqualTo(SC_UNAUTHORIZED);

        assertThat(isValidRequest).isFalse();
    }

    @Test
    void shouldThrowInvalidAuthorizationExceptionWhenAccessingCaseActionFromIncorrectRole() throws IOException {
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        final Object object = mock(Object.class);
        List<String> roles = Arrays.asList(AUTHORISATION_SERVICE_LOGS_ROLE);

        doNothing().when(serviceAuthorizationAuthenticator).authorizeServiceToken(httpServletRequest);
        when(httpServletRequest.getMethod()).thenReturn(GET.name());
        when(authorizationAuthenticator.authorizeAuthorizationToken(httpServletRequest)).thenReturn(roles);
        when(httpServletRequest.getParameter(CASE_ACTION)).thenReturn(CaseAction.VIEW.name());

        final boolean isValidRequest = restApiPreInvokeInterceptor
            .preHandle(httpServletRequest, httpServletResponse, object);

        assertThat(((MockHttpServletResponse) httpServletResponse).getErrorMessage())
            .isEqualTo("Not authorised to make HTTP GET request for CaseAction: VIEW");
        assertThat(((MockHttpServletResponse) httpServletResponse).getStatus())
            .isEqualTo(SC_UNAUTHORIZED);

        assertThat(isValidRequest).isFalse();
    }
}
