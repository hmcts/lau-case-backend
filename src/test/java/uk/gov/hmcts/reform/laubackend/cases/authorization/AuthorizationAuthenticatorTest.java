package uk.gov.hmcts.reform.laubackend.cases.authorization;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidAuthorizationException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_HEADER;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorizationAuthenticatorTest {


    @Mock
    private AuthService authService;

    @Mock
    private AuthorisedServices authorisedServices;

    @InjectMocks
    private AuthorizationAuthenticator authorizationAuthenticator;

    @Test
    void shouldThrowExceptionForInvalidServiceName() {
        final String header = "Super cool header";
        final UserInfo userInfo = mock(UserInfo.class);
        final List<String> userRoles = List.of("test role");
        try {
            final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

            when(httpServletRequest.getHeader(AUTHORISATION_HEADER)).thenReturn(header);
            when(authService.authorize(header)).thenReturn(userInfo);
            when(userInfo.getRoles()).thenReturn(userRoles);
            when(authorisedServices.hasRole(userInfo.getRoles())).thenReturn(false);
            when(userInfo.getName()).thenReturn("Mickey");
            when(userInfo.getFamilyName()).thenReturn("Mouse");

            authorizationAuthenticator.authorizeAuthorizationToken(httpServletRequest);

            fail("The method should have thrown InvalidAuthorizationException");
        } catch (final InvalidAuthorizationException invalidAuthorizationException) {
            assertThat(invalidAuthorizationException.getMessage()).isEqualTo("Unable to authorize user.");
        }
    }

    @Test
    void shouldNotThrowExceptionForValidServiceName() {
        final String header = "Super cool header";
        final UserInfo userInfo = mock(UserInfo.class);
        final List<String> userRoles = List.of("test role");
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        when(httpServletRequest.getHeader(AUTHORISATION_HEADER)).thenReturn(header);
        when(authService.authorize(header)).thenReturn(userInfo);
        when(userInfo.getRoles()).thenReturn(userRoles);
        when(authorisedServices.hasRole(userInfo.getRoles())).thenReturn(true);

        assertDoesNotThrow(() -> authorizationAuthenticator.authorizeAuthorizationToken(httpServletRequest));
    }

}
