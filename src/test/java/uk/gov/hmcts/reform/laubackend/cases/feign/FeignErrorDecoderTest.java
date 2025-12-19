package uk.gov.hmcts.reform.laubackend.cases.feign;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Response;
import feign.RetryableException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.laubackend.cases.authorization.HttpPostRecordHolder;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeignErrorDecoderTest {

    private static final String GET_METHOD = "GET";
    private static final String DETAILS_URL = "http://localhost/service/details";
    private static final String METHOD_KEY = "methodKey";

    @Mock
    private HttpPostRecordHolder httpPostRecordHolder;

    @InjectMocks
    private FeignErrorDecoder feignErrorDecoder;

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

    @ParameterizedTest
    @CsvSource({
        "200,GET,http://localhost/service/details",
        "308,GET,http://localhost/service/details",
        "401,GET,http://localhost/service/details",
        "401,GET,http://localhost/api?endpoint=details&user=123",
        "401,POST,http://localhost/service/details",
        "403,GET,http://localhost/service/validate",
        "403,GET,http://localhost/api/v1/details/user",
        "403,PUT,http://localhost/service/details",
        "500,GET,http://localhost/service/details",
        "500,GET,http://localhost/service/validate",
        "500,GET,http://localhost/api?endpoint=details&user=123",
        "502,PUT,http://localhost/api?endpoint=details&user=123",
        "502,PUT,http://localhost/service/details",
        "504,POST,http://localhost/service/details"
    })
    void shouldReturnNotRetryableException(int httpStatus, String httpMethod, String url) {
        try (Response response = buildResponse(httpStatus, httpMethod, url)) {
            Exception ex = feignErrorDecoder.decode(METHOD_KEY, response);
            assertThat(ex)
                .isInstanceOf(FeignException.class)
                .isNotInstanceOf(RetryableException.class);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {500, 502, 503, 504})
    void shouldReturnRetryableException(int httpStatus) {
        try (Response response = buildResponse(httpStatus, GET_METHOD, DETAILS_URL)) {
            when(httpPostRecordHolder.isPost()).thenReturn(true);
            Exception ex = feignErrorDecoder.decode(METHOD_KEY, response);
            assertThat(ex).isInstanceOf(RetryableException.class);
        }
    }

}
