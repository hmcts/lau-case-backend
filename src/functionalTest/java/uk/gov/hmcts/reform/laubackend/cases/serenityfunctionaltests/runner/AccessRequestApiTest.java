package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.runner;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.AccessRequestGetApiSteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.AccessRequestPostApiSteps;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.helper.DatabaseCleaner.deleteAccessRequestRecord;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_ACCESS_REQUEST_DELETE_ENDPOINT;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_ACCESS_REQUEST_ENDPOINT;

@SuppressWarnings("PMD.LawOfDemeter")
@ExtendWith(SerenityJUnit5Extension.class)
class AccessRequestApiTest {

    @Steps
    AccessRequestGetApiSteps getSteps;

    @Steps
    AccessRequestPostApiSteps postSteps;

    @Test
    @DisplayName("Assert response code of 200 for GET /audit/accessRequest with valid headers and valid request params")
    void assertHttpSuccessResponseCodeForGetAccessRequestApi() throws JSONException {
        String serviceToken = getSteps.givenAValidServiceTokenIsGenerated();
        String authorizationToken = getSteps.validAuthorizationTokenIsGenerated();
        JSONObject postRequestBody = postSteps.generateAccessRequestPostRequestBodyJson();
        Response postResponse = postSteps.performPostOperation(
            AUDIT_ACCESS_REQUEST_ENDPOINT, postRequestBody.toString(), serviceToken);
        postResponse.getBody();
        Map<String, String> queryParams = getSteps.createRequestQueryParams(postResponse.getBody().asString());

        Response response = getSteps.whenTheGetAccessRequestServiceIsInvokedWithTheGivenParams(
            serviceToken, authorizationToken, queryParams);
        getSteps.assertSuccessResponse(response, postRequestBody.getJSONObject("accessLog"), HttpStatus.SC_OK);

        deleteAccessRequestRecord(AUDIT_ACCESS_REQUEST_DELETE_ENDPOINT, postResponse);
    }

    @Test
    @DisplayName("Assert that response code of 201 is returned for POST /audit/accessRequest")
    void assertHttpSuccessResponseCodeForPostAccessRequestApi() throws JSONException {
        String serviceToken = postSteps.givenAValidServiceTokenIsGenerated();
        String requestBody = postSteps.generateAccessRequestPostRequestBodyJson().toString();
        Response response = postSteps.performPostOperation(AUDIT_ACCESS_REQUEST_ENDPOINT, requestBody, serviceToken);
        assertResponse(response, HttpStatus.SC_CREATED);

        deleteAccessRequestRecord(AUDIT_ACCESS_REQUEST_DELETE_ENDPOINT, response);
    }

    @Test
    @DisplayName("Assert response code 400 for GET /audit/accessRequest with empty query params")
    void assertBadRequestResponseCodeForGetAccessRequestApi() throws JSONException {
        String serviceToken = getSteps.givenAValidServiceTokenIsGenerated();
        String authorizationToken = getSteps.validAuthorizationTokenIsGenerated();
        Response response = getSteps.whenTheGetAccessRequestServiceIsInvokedWithTheGivenParams(
            serviceToken, authorizationToken, null);
        assertResponse(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Assert response code 401 for GET /audit/accessRequest with invalid auth token")
    void assertUnauthorizedResponseCodeForGetAccessRequestApi() {
        String serviceToken = getSteps.givenAValidServiceTokenIsGenerated();

        Response response = getSteps.whenTheGetAccessRequestServiceIsInvokedWithTheGivenParams(
            serviceToken, "invalid-token", null);
        assertResponse(response, HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Assert response code 403 for GET /audit/accessRequest with invalid service token")
    void assertForbiddenResponseCodeForGetAccessRequestApi() throws JSONException {
        String authorizationToken = getSteps.validAuthorizationTokenIsGenerated();
        Response response = getSteps.whenTheGetAccessRequestServiceIsInvokedWithTheGivenParams(
            "invalid-token", authorizationToken, null);
        assertResponse(response, HttpStatus.SC_FORBIDDEN);
    }

    private void assertResponse(Response response, int expectedStatusCode) {
        String failMsg = "Expected status code to be " + expectedStatusCode + " but was " + response.getStatusCode();

        assertThat(response.statusCode())
            .withFailMessage(failMsg)
            .isEqualTo(expectedStatusCode);
    }


}
