package uk.gov.hmcts.reform.laubackend.cases.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncAuthService {

    private final AuthTokenValidator authTokenValidator;

    @Autowired
    public AsyncAuthService(final AuthTokenValidator authTokenValidator) {
        this.authTokenValidator = authTokenValidator;
    }

    public CompletableFuture<String> authenticateService(final String authHeader) {
        if (authHeader != null) {
            return CompletableFuture.completedFuture(authTokenValidator.getServiceName(authHeader));
        }
        throw new InvalidServiceAuthorizationException("Missing ServiceAuthorization header");
    }
}
