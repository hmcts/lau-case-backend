package uk.gov.hmcts.reform.laubackend.cases.helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.AUTH_TOKEN;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.SERVICE_AUTHORISATION_HEADER;

public class RestHelper {

    public static Response getResponseWithoutHeader(final String path) {

        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when()
                .get()
                .andReturn();
    }

    public Response getResponse(final String path,
                                final String parameterName,
                                final String parameterValue) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .queryParam(parameterName, parameterValue)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .get()
                .andReturn();
    }

    public Response postObject(final Object object,
                               final String path) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .body(object)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .post()
                .andReturn();
    }

    public Response postObjectWithoutHeader(final Object object,
                                            final String path) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .body(object)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when()
                .post()
                .andReturn();
    }
}
