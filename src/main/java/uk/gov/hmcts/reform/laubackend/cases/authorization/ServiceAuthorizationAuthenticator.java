package uk.gov.hmcts.reform.laubackend.cases.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import javax.servlet.http.HttpServletRequest;

import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;

@Slf4j
@Service
public class ServiceAuthorizationAuthenticator {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthorisedServices authorisedServices;

    public void authorizeServiceToken(final HttpServletRequest request) {
        final String serviceAuthHeader = request.getHeader(SERVICE_AUTHORISATION_HEADER);
        final String serviceName = authService.authenticateService(serviceAuthHeader);

        if (!authorisedServices.hasService(serviceName)) {
            log.info("Service {} has NOT been authorised!", serviceName);
            throw new InvalidServiceAuthorizationException("Unable to authenticate service name.");
        }
    }
}
