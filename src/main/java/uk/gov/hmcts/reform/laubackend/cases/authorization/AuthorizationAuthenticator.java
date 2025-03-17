package uk.gov.hmcts.reform.laubackend.cases.authorization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidAuthorizationException;

import java.util.List;

import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_HEADER;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.PreserveStackTrace", "PMD.ExceptionAsFlowControl"})
public class AuthorizationAuthenticator {

    private final AuthService authService;

    private final AuthorisedServices authorisedServices;

    public List<String> authorizeAuthorizationToken(final HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader(AUTHORISATION_HEADER);
            final UserInfo userInfo = authService.authorize(authHeader);

            if (!authorisedServices.hasRole(userInfo.getRoles())) {
                log.info("User {} has NOT been authorised!", userInfo.getName().concat(" ")
                        .concat(userInfo.getFamilyName()));
                throw new InvalidAuthorizationException("Unable to authorize user.");
            }
            return userInfo.getRoles();

        } catch (final Exception exception) {
            throw new InvalidAuthorizationException(exception.getMessage());
        }
    }
}
