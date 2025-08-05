package uk.gov.hmcts.reform.laubackend.cases.feign;

import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.laubackend.cases.authorization.HttpPostRecordHolder;

@Configuration
@Slf4j
public class FeignErrorDecoder implements feign.codec.ErrorDecoder {

    @Autowired
    private HttpPostRecordHolder httpPostRecordHolder;

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus respStatus = HttpStatus.valueOf(response.status());
        FeignException exception = FeignException.errorStatus(methodKey, response);
        log.info("Feign response status: {}, message - {}", response.status(), exception.getMessage());
        if (respStatus.is5xxServerError()
            && Request.HttpMethod.GET.equals(response.request().httpMethod())
            && response.request().url().endsWith("/details")
            && httpPostRecordHolder.isPost()) {
            log.info("Going to throw RetryableException: {}", response.status());
            return new RetryableException(
                response.status(),
                exception.getMessage(),
                response.request().httpMethod(),
                (Long) null, // unix timestamp *at which time* the request can be retried
                response.request()
            );
        }
        return exception;
    }
}
