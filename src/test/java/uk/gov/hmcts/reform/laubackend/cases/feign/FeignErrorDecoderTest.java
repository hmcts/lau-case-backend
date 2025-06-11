package uk.gov.hmcts.reform.laubackend.cases.feign;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Response;
import feign.RetryableException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"PMD.CloseResource"})
class FeignErrorDecoderTest {

    private static final String GET_METHOD = "GET";
    private static final String DETAILS_URL = "http://localhost/service/details";
    private static final String METHOD_KEY = "methodKey";

    private final FeignErrorDecoder decoder = new FeignErrorDecoder();

    private Response buildResponse(int status, String method, String url) {
        Request request = Request.create(
            HttpMethod.valueOf(method),
            url,
            Collections.emptyMap(),
            null,
            StandardCharsets.UTF_8,
            null
        );
        return Response.builder()
            .status(status)
            .request(request)
            .build();
    }

    @Test
    void shouldReturnRetryableExceptionForGetRequestWithDetailsUrlAndStatus400() {
        Response response = buildResponse(401, GET_METHOD, DETAILS_URL);
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(RetryableException.class);
    }

    @Test
    void shouldReturnRetryableExceptionForGetRequestWithDetailsUrlAndStatus500() {
        Response response = buildResponse(500, GET_METHOD, DETAILS_URL);
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(RetryableException.class);
    }

    @Test
    void shouldReturnFeignExceptionForGetRequestWithoutDetailsUrl() {
        Response response = buildResponse(403, GET_METHOD, "http://localhost/service/validate");
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(FeignException.class)
            .isNotInstanceOf(RetryableException.class);
    }

    @Test
    void shouldReturnFeignExceptionForPostRequestEvenWithDetailsUrl() {
        Response response = buildResponse(401, "POST", DETAILS_URL);
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(FeignException.class)
            .isNotInstanceOf(RetryableException.class);
    }

    @Test
    void shouldReturnFeignExceptionForPutRequestWithDetailsUrl() {
        Response response = buildResponse(403, "PUT", DETAILS_URL);
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(FeignException.class)
            .isNotInstanceOf(RetryableException.class);
    }

    @Test
    void shouldReturnFeignExceptionForGetRequestWithDetailsUrlAndStatus200() {
        Response response = buildResponse(200, GET_METHOD, DETAILS_URL);
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(FeignException.class)
            .isNotInstanceOf(RetryableException.class);
    }

    @Test
    void shouldReturnFeignExceptionForGetRequestWithDetailsUrlAndStatus399() {
        Response response = buildResponse(399, GET_METHOD, DETAILS_URL);
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(FeignException.class)
            .isNotInstanceOf(RetryableException.class);
    }

    @Test
    void shouldReturnRetryableExceptionForGetRequestWithDetailsInPath() {
        Response response = buildResponse(403, GET_METHOD, "http://localhost/api/v1/details/user");
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(FeignException.class)
            .isNotInstanceOf(RetryableException.class);
    }

    @Test
    void shouldReturnRetryableExceptionForGetRequestWithDetailsAsQueryParam() {
        Response response = buildResponse(401, GET_METHOD, "http://localhost/api?endpoint=details&user=123");
        Exception ex = decoder.decode(METHOD_KEY, response);
        assertThat(ex).isInstanceOf(FeignException.class)
            .isNotInstanceOf(RetryableException.class);
    }
}
