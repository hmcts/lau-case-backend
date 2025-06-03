package uk.gov.hmcts.reform.laubackend.cases.authorization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.PreserveStackTrace","PMD.DoNotUseThreads","PMD.UseTryWithResources",
    "PMD.ExceptionAsFlowControl"})
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
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        try {
            CompletableFuture<Void> future = asyncAuthService.authenticateService(serviceAuthHeader)
                .thenAcceptAsync(serviceName -> {
                    if (!authorisedServices.hasService(serviceName)) {
                        log.error("Service name '{}' is not authorized.", serviceName);
                        throw new InvalidServiceAuthorizationException(
                            "Unable to authenticate service name asynchronously.");
                    }
                }, executor)
                .handle((result, ex) -> {
                    if (ex != null) {
                        log.error("Error during service authentication: {}", ex.getMessage(), ex);
                        throw new CompletionException(ex.getCause());
                    }
                    return result;
                });

            future.join();  // Wait for the asynchronous operation to complete.
        } catch (CompletionException ex) {
            handleCompletionException(ex);
        } finally {
            executor.close(); // Ensure the executor is closed to release resources
        }
    }

    private void handleOtherRequest(String serviceAuthHeader) {
        try {
            final String serviceName = String.valueOf(authService.authenticateService(serviceAuthHeader));
            if (!authorisedServices.hasService(serviceName)) {
                throw new InvalidServiceAuthorizationException("Unable to authenticate service name.");
            }
        } catch (Exception exception) {
            throw new InvalidServiceAuthorizationException(exception.getMessage());
        }
    }

    private void handleCompletionException(CompletionException ex) {
        if (ex.getCause() instanceof InvalidServiceAuthorizationException) {
            log.error("Invalid service authorization: {}", ex.getCause().getMessage(), ex);
            throw (InvalidServiceAuthorizationException) ex.getCause();
        }
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        throw new InvalidServiceAuthorizationException(ex.getMessage());
    }
}
