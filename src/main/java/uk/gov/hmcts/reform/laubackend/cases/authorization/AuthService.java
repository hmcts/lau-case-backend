package uk.gov.hmcts.reform.laubackend.cases.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidAuthenticationException;

@Component
public class AuthService {

    private final AuthTokenValidator authTokenValidator;

    @Autowired
    public AuthService(final AuthTokenValidator authTokenValidator) {
        this.authTokenValidator = authTokenValidator;
    }

    public String authenticate(String authHeader) {
        if (authHeader != null) {
            return authTokenValidator.getServiceName(authHeader);
        }
        throw new InvalidAuthenticationException("Missing ServiceAuthorization header");
    }
}
