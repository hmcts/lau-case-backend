package uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.config.EnvConfig;
import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.utils.TestConstants;


import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public class BaseSteps {


    public static final int FAILED_POST_DELAY_MS = 2000;
    private static final RequestSpecification spec;
    private final static Logger log = LoggerFactory.getLogger((uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.steps.BaseSteps.class));
    private static final int MAX_PAGES = 3;
    private final String s2sName = TestConstants.S2S_NAME;
    private final String s2sSecret = TestConstants.S2S_SECRET;
    private final String s2sUrl = TestConstants.S2S_URL;

    static {
        final String proxyHost = System.getProperty("http.proxyHost");
        final Integer proxyPort = proxyHost == null ? null : Integer.parseInt(System.getProperty("http.proxyPort"));

        final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset(StandardCharsets.UTF_8));

        final RequestSpecBuilder specBuilder = new RequestSpecBuilder()
            .setConfig(config)
            .setBaseUri(TestConstants.BASE_URL)
            .setRelaxedHTTPSValidation();

        log.info("Using base API URL: " + EnvConfig.API_URL);
        if (proxyHost != null) {
            specBuilder.setProxy(proxyHost, proxyPort);
        }

        spec = specBuilder.build();
    }

    public RequestSpecification rest() {
        return SerenityRest.given(spec);
    }

    public RequestSpecification given() {
        return SerenityRest.given(spec);
    }


    public String getServiceToken(String s2sMicroServiceName) {

        log.info("s2sUrl lease url: {}", s2sUrl + "/lease");
        final Map<String, Object> params = of(
            "microservice", s2sMicroServiceName
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
        System.out.println("Token Resp : " + response.body().asString());
        assertThat(response.getStatusCode()).isEqualTo(200);

        return "Bearer " + response
            .getBody()
            .asString();
    }

    public Response performGetOperation(String endpoint,
                                        Map<String, String> headers,
                                        Map<String, String> queryParams, String authServiceToken) {

        RequestSpecification requestSpecification = rest().urlEncodingEnabled(false)
            .given().header("ServiceAuthorization",  authServiceToken)
            .header("Content-Type", "application/json");


        if (null != headers && !headers.isEmpty()) {
            for (String headerKey : headers.keySet()) {
                requestSpecification.header(new Header(headerKey, headers.get(headerKey)));
            }
        }

        if (null != queryParams && !queryParams.isEmpty()) {
            for (String queryParamKey : queryParams.keySet()) {
                requestSpecification.queryParam(queryParamKey, queryParams.get(queryParamKey));
            }
        }

        return requestSpecification.get(endpoint)
            .then()
            .extract().response();
    }


    public Response performPostOperation(String endpoint,
                                         Map<String, String> headers,
                                         Map<String, String> queryParams,
                                         Object body,
                                         String authServiceToken
    ) {

        String bodyJsonStr = null;
        if (null != body) {
            try {
                bodyJsonStr = new ObjectMapper().writeValueAsString(body);
                System.out.println("the body string is - "  + bodyJsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestSpecification requestSpecification = rest()
            .given().header("ServiceAuthorization", authServiceToken)
            .header("Content-Type", "application/json");

        if (null != headers && !headers.isEmpty()) {
            for (String headerKey : headers.keySet()) {
                requestSpecification.header(new Header(headerKey, headers.get(headerKey)));
            }
        }
        if (null != queryParams && !queryParams.isEmpty()) {
            for (String queryParamKey : queryParams.keySet()) {
                requestSpecification.param(queryParamKey, queryParams.get(queryParamKey));
            }
        }
        return requestSpecification.urlEncodingEnabled(true).body(bodyJsonStr).post(endpoint)
            .then()
            .extract().response();
    }

}
