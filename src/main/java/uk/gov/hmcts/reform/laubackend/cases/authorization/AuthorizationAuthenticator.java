package uk.gov.hmcts.reform.laubackend.cases.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidAuthorizationException;

import javax.servlet.http.HttpServletRequest;

import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_HEADER;

@Slf4j
@Service
public class AuthorizationAuthenticator {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthorisedServices authorisedServices;

    public void authorizeAuthorizationToken(final HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORISATION_HEADER);
        final UserInfo userInfo = authService.authorize(authHeader);

        if (!authorisedServices.hasRole(userInfo.getRoles())) {
            log.info("User {} has NOT been authorised!", userInfo.getName().concat(" ")
                    .concat(userInfo.getFamilyName()));
            throw new InvalidAuthorizationException("Unable to authorize user.");
        }
    }
}
