package uk.gov.hmcts.reform.laubackend.cases.authorization;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncAuthService {

    private final AuthTokenValidator authTokenValidator;
    private final IdamClient idamClient;

    @Async("taskExecutor")
    public CompletableFuture<String> getServiceName(String authHeader) {
        log.info("getServiceName running in thread: {}", Thread.currentThread());

        if (authHeader != null) {
            return CompletableFuture.completedFuture(
                authTokenValidator.getServiceName(authHeader)
            );
        }
        return CompletableFuture.failedFuture(
            new InvalidServiceAuthorizationException("Missing Authorization header"));
    }

    @Async("taskExecutor")
    public CompletableFuture<UserInfo> getUserInfo(String authHeader) {
        log.info("getUserInfo running in thread: {}", Thread.currentThread());

        if (authHeader != null) {
            return CompletableFuture.completedFuture(idamClient.getUserInfo(authHeader));
        }
        return CompletableFuture.failedFuture(
            new InvalidServiceAuthorizationException("Missing Authorization header"));
    }
}
