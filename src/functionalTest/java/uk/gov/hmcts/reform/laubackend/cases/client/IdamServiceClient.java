package uk.gov.hmcts.reform.laubackend.cases.client;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class IdamServiceClient {

    private final String s2sName;
    private final String s2sSecret;
    private final String s2sUrl;

    public IdamServiceClient(final String s2sName, final String s2sSecret, final String s2sUrl) {
        this.s2sName = s2sName;
        this.s2sSecret = s2sSecret;
        this.s2sUrl = s2sUrl;
    }

    @SuppressWarnings("PMD.UseConcurrentHashMap")
    public String s2sSignIn() {

        log.info("s2sUrl lease url: {}", s2sUrl + "/lease");
        final Map<String, Object> params = of(
                "microservice", s2sName,
                "oneTimePassword", new GoogleAuthenticator().getTotpPassword(s2sSecret)
        );

        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(s2sUrl)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/lease")
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(200);

        return response
                .getBody()
                .asString();
    }
}
