package uk.gov.hmcts.reform.laubackend.cases.authorization;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceAuthorizationAuthenticatorTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthorisedServices authorisedServices;

    @InjectMocks
    private ServiceAuthorizationAuthenticator serviceAuthorizationAuthenticator;

    @Test
    void shouldThrowExceptionForInvalidServiceName() {
        final String header = "Super cool header";
        final String serviceName = "super_cool_service";
        try {
            final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
            when(httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER)).thenReturn(header);
            when(authService.authenticateService(header)).thenReturn(serviceName);

            when(authorisedServices.hasService(serviceName)).thenReturn(false);

            serviceAuthorizationAuthenticator.authorizeServiceToken(httpServletRequest);

            fail("The method should have thrown InvalidServiceAuthorizationException");
        } catch (final InvalidServiceAuthorizationException invalidServiceAuthorizationException) {
            assertThat(invalidServiceAuthorizationException.getMessage())
                    .isEqualTo("Unable to authenticate service name.");
        }
    }

    @Test
    void shouldNotThrowExceptionForValidServiceName() {
        final String header = "Super cool header";
        final String serviceName = "super_cool_service";

        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER)).thenReturn(header);
        when(authService.authenticateService(header)).thenReturn(serviceName);

        when(authorisedServices.hasService(serviceName)).thenReturn(true);

        serviceAuthorizationAuthenticator.authorizeServiceToken(httpServletRequest);

        assertDoesNotThrow(() -> serviceAuthorizationAuthenticator.authorizeServiceToken(httpServletRequest));
    }
}
