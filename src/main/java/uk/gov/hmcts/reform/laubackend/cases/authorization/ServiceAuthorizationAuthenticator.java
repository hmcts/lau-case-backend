package uk.gov.hmcts.reform.laubackend.cases.authorization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;
import static org.springdoc.core.utils.Constants.POST_METHOD;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.PreserveStackTrace"})
public class ServiceAuthorizationAuthenticator {

    private final AuthService authService;

    private final AuthorisedServices authorisedServices;

    private final HttpPostRecordHolder httpPostRecordHolder;

    public void authorizeServiceToken(HttpServletRequest httpServletRequest) {
        String serviceAuthHeader = httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER);
        String method = httpServletRequest.getMethod();

        if (POST_METHOD.equalsIgnoreCase(method)) {
            httpPostRecordHolder.setPost(true);
        } else {
            httpPostRecordHolder.setPost(false);
        }
        final String serviceName = String.valueOf(authService.authenticateService(serviceAuthHeader));
        if (!authorisedServices.hasService(serviceName)) {
            log.info("Service {} has NOT been authorised!", serviceName);
            throw new InvalidServiceAuthorizationException("Unable to authenticate service name.");
        }
    }
}
