package uk.gov.hmcts.reform.laubackend.cases.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidAuthorizationException;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthService {

private final AsyncAuthService asyncAuthService;



    public String authenticateService(final String authHeader) {
        try {
            return asyncAuthService.getServiceName(authHeader).get();
        } catch (final Exception e) {
            throw new InvalidAuthorizationException("Authorization failed: " + e.getMessage());

        }
    }

    public UserInfo authorize(final String authHeader) {
        try {
            return asyncAuthService.getUserInfo(authHeader).get();
        } catch (final Exception e) {
            throw new InvalidAuthorizationException("Authorization failed: " + e.getMessage());
        }

    }

//
//    @Async("taskExecutor")
//    public CompletableFuture<UserInfo> getUserInfo(String authHeader) {
//        String threadName = Thread.currentThread().getName();
//        log.info("********* getUserInfo executing in thread ****************: {}", threadName);
//
//        if (authHeader != null) {
//            return CompletableFuture.completedFuture(asyncAuthService.getUserInfo(authHeader));
//        }
//        return CompletableFuture.failedFuture(
//            new InvalidServiceAuthorizationException("Missing Authorization header"));
//    }
//
//
//    @Async("taskExecutor")
//    public CompletableFuture<String> getServiceName(String authHeader) {
//        String threadName = Thread.currentThread().getName();
//        log.info("***************** getServiceName executing in thread *****************: {}", threadName);
//        if (authHeader != null) {
//            return CompletableFuture.completedFuture(
//                authTokenValidator.getServiceName(authHeader)
//            );
//        }
//        return CompletableFuture.failedFuture(
//            new InvalidServiceAuthorizationException("Missing Authorization header"));
//    }
}

