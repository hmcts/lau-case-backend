package uk.gov.hmcts.reform.laubackend.cases.helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.AUTH_TOKEN;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.SERVICE_AUTHORISATION_HEADER;

@SuppressWarnings({"unchecked", "PMD.AvoidDuplicateLiterals"})
public class RestHelper {

    public Response getResponseWithoutHeader(final String path) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when()
                .get()
                .andReturn();
    }

    public Response getResponseWithoutAuthorizationHeader(final String path) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .get()
                .andReturn();
    }


    public Response getResponse(final String path,
                                final Map<String, String> queryParams) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .queryParams(queryParams)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .header(AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .get()
                .andReturn();
    }


    public Response deleteResponse(final String path,
                                   final String parameterName,
                                   final String parameterValue) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .queryParam(parameterName, parameterValue)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .header(AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .delete()
                .andReturn();
    }

    public Response deleteResponseWithoutServiceAuthHeader(final String path) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .delete()
                .andReturn();
    }

    public Response deleteResponseWithoutAuthHeader(final String path) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(path)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .delete()
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

    public Response postObjectAsString(final String requestBody, final String path) {
        return RestAssured
            .given()
            .relaxedHTTPSValidation()
            .baseUri(path)
            .body(requestBody)
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
