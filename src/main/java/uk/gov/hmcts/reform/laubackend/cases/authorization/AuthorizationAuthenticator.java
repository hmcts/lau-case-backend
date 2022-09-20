package uk.gov.hmcts.reform.laubackend.cases.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidAuthorizationException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_HEADER;

@Slf4j
@Service
@SuppressWarnings({"PMD.PreserveStackTrace"})
public class AuthorizationAuthenticator {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthorisedServices authorisedServices;

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
