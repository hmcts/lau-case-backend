package uk.gov.hmcts.reform.laubackend.cases.authorization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import java.util.concurrent.ExecutionException;

import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.PreserveStackTrace", "PMD.ExceptionAsFlowControl",
    "PMD.DoNotUseThreads"})
public class ServiceAuthorizationAuthenticator {

    private final AuthService authService;

    private final AuthorisedServices authorisedServices;

    private final AsyncAuthService asyncAuthService;

    public static final String POST_METHOD = "POST";

    public void authorizeServiceToken(HttpServletRequest httpServletRequest) {
        String serviceAuthHeader = httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER);
        String method = httpServletRequest.getMethod();

        if (POST_METHOD.equalsIgnoreCase(method)) {
            handlePostRequest(serviceAuthHeader);
        } else {
            handleOtherRequest(serviceAuthHeader);
        }
    }

    private void handlePostRequest(String serviceAuthHeader) {
        try {
            String serviceName = asyncAuthService.authenticateService(serviceAuthHeader).get();

            if (!authorisedServices.hasService(serviceName)) {
                log.info("Service {} has NOT been authorised!", serviceName);
                throw new InvalidServiceAuthorizationException(
                    "Unable to authenticate service name in Post request.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InvalidServiceAuthorizationException("Thread interrupted" + e.getMessage());
        } catch (ExecutionException e) {
            Throwable cause = e.getCause(); // Extract the original cause
            if (cause instanceof InvalidServiceAuthorizationException) {
                throw (InvalidServiceAuthorizationException) cause; // Rethrow if it's the expected exception
            }
            throw new InvalidServiceAuthorizationException("Service authentication failed: " + cause.getMessage());
        }
    }

    private void handleOtherRequest(String serviceAuthHeader) {
        try {
            final String serviceName = String.valueOf(authService.authenticateService(serviceAuthHeader));
            if (!authorisedServices.hasService(serviceName)) {
                log.info("Service {} has NOT been authorised!", serviceName);
                throw new InvalidServiceAuthorizationException("Unable to authenticate service name.");
            }
        } catch (final Exception exception) {
            throw new InvalidServiceAuthorizationException(exception.getMessage());
        }
    }
}
