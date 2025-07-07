package uk.gov.hmcts.reform.laubackend.cases.authorization;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;

@ExtendWith(MockitoExtension.class)
class ServiceAuthorizationAuthenticatorTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthorisedServices authorisedServices;

    @Mock
    private HttpPostRecordHolder httpPostRecordHolder;

    @InjectMocks
    private ServiceAuthorizationAuthenticator serviceAuthorizationAuthenticator;

    public static final String HEADER = "Super cool header";



    @Test
    void shouldThrowExceptionForInvalidServiceName() {
        final String serviceName = "super_cool_service";

        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER)).thenReturn(HEADER);
        when(authService.authenticateService(HEADER)).thenReturn(serviceName);

        when(authorisedServices.hasService(serviceName)).thenReturn(false);

        Throwable thrown = catchThrowable(() ->
            serviceAuthorizationAuthenticator.authorizeServiceToken(httpServletRequest));

        assertThat(thrown)
            .isInstanceOf(InvalidServiceAuthorizationException.class)
            .hasMessage("Unable to authenticate service name.");
    }

    @Test
    void shouldNotThrowExceptionForValidServiceName() {
        final String serviceName = "super_cool_service";

        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER)).thenReturn(HEADER);
        when(authService.authenticateService(HEADER)).thenReturn(serviceName);

        when(authorisedServices.hasService(serviceName)).thenReturn(true);

        serviceAuthorizationAuthenticator.authorizeServiceToken(httpServletRequest);

        assertDoesNotThrow(() -> serviceAuthorizationAuthenticator.authorizeServiceToken(httpServletRequest));
    }

    @Test
    void shouldNotThrowExceptionForValidServiceNameOnPost() {
        final String serviceName = "super_cool_service";

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER)).thenReturn(HEADER);
        when(httpServletRequest.getMethod()).thenReturn("POST");

        when(authService.authenticateService(HEADER)).thenReturn(serviceName);
        when(authorisedServices.hasService(serviceName)).thenReturn(true);

        // Should not throw any exception
        assertDoesNotThrow(() -> serviceAuthorizationAuthenticator.authorizeServiceToken(httpServletRequest));
    }
}
