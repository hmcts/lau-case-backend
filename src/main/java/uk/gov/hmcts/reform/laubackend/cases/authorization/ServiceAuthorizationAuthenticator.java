package uk.gov.hmcts.reform.laubackend.cases.authorization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import java.util.concurrent.CompletionException;

import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.PreserveStackTrace", "PMD.ExceptionAsFlowControl", "PMD.CyclomaticComplexity"})
public class ServiceAuthorizationAuthenticator {

    private final AuthService authService;

    private final AuthorisedServices authorisedServices;

    private final AsyncAuthService asyncAuthService;

    public static final String POST_METHOD = "POST";

    public void authorizeServiceToken(HttpServletRequest httpServletRequest) {
        String serviceAuthHeader = httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER);
        String method = httpServletRequest.getMethod();

        if (POST_METHOD.equalsIgnoreCase(method)) {
            try {
                asyncAuthService.authenticateService(serviceAuthHeader)
                .thenAccept(serviceName -> {
                    if (!authorisedServices.hasService(serviceName)) {
                        throw new InvalidServiceAuthorizationException(
                            "Unable to authenticate service name asynchronously.");
                    }
                })
                .exceptionally(ex -> {
                    throw new CompletionException(ex.getCause());
                }).join();
            } catch (CompletionException ex) {
                if (ex.getCause() instanceof InvalidServiceAuthorizationException) {
                    throw (InvalidServiceAuthorizationException) ex.getCause();
                }
                throw new InvalidServiceAuthorizationException(ex.getMessage()); // Rethrow the exception to the caller
            }
        } else {
            try {
                final String serviceName = String.valueOf(authService.authenticateService(serviceAuthHeader));
                if (!authorisedServices.hasService(serviceName)) {
                    throw new InvalidServiceAuthorizationException("Unable to authenticate service name.");
                }
            } catch (Exception exception) {
                throw new InvalidServiceAuthorizationException(exception.getMessage());
            }
        }
    }
}
