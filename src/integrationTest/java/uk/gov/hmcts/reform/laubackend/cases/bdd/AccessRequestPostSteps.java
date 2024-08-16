package uk.gov.hmcts.reform.laubackend.cases.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestPostRequest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static uk.gov.hmcts.reform.laubackend.cases.helper.AccessRequestPostHelper.getAccessRequestPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.AccessRequestPostHelper.mapToAccessRequestPostRequestBody;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequestWithInvalidParameter;

public class AccessRequestPostSteps extends AbstractSteps {

    private int httpStatusResponseCode;
    private String accessRequestPostResponseBody;

    @Before
    public void setUp() {
        setupAuthorisationStub();
    }

    @When("I POST access request using {string} endpoint using s2s")
    public void postAccessRequest(final String path) {
        final Response response = restHelper.postObject(getAccessRequestPostRequest(), baseUrl() + path);
        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        accessRequestPostResponseBody = response.getBody().asString();
    }

    @Then("accessRequest response body is returned")
    public void accessRequestResponseBodyIsReturned() {
        final AccessRequestPostRequest request = getAccessRequestPostRequest();

        assertResponse(request);
    }

    @When("POST to {string} endpoint with missing required body parameter using s2s returns Bad Request")
    public void postAccessRequestMissingParameter(String path, List<Map<String, String>> data) {
        for (Map<String, String> map : data) {
            String requestBody = mapToAccessRequestPostRequestBody(map);
            final Response response = restHelper.postObject(requestBody, baseUrl() + path);
            assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST.value());
        }
    }

    @When("I POST accessRequest {string} endpoint with missing s2s header")
    public void postCaseActionWithMissingAuthHeader(String path) {
        final Response response = restHelper
            .postObjectWithoutHeader(getCaseActionPostRequestWithInvalidParameter(), baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }


    @Then("http forbidden response is returned for POST accessRequest")
    public void httpForbiddenResponseIsReturnedForPostAccessRequest() {
        assertThat(httpStatusResponseCode).isEqualTo(FORBIDDEN.value());
    }

    private void assertResponse(final AccessRequestPostRequest request) {
        final AccessRequestPostRequest response = jsonReader.fromJson(
            accessRequestPostResponseBody, AccessRequestPostRequest.class);

        assertThat(response).isNotNull();
        AccessRequestLog accessRequestLog = response.getAccessRequestLog();
        assertThat(accessRequestLog).isNotNull();

        assertThat(accessRequestLog.getRequestType()).isEqualTo(request.getAccessRequestLog().getRequestType());
        assertThat(accessRequestLog.getUserId()).isEqualTo(request.getAccessRequestLog().getUserId());
        assertThat(accessRequestLog.getCaseRef()).isEqualTo(request.getAccessRequestLog().getCaseRef());
        assertThat(accessRequestLog.getReason()).isEqualTo(request.getAccessRequestLog().getReason());
        assertThat(accessRequestLog.getAction()).isEqualTo(request.getAccessRequestLog().getAction());
        assertThat(accessRequestLog.getTimeLimit()).isEqualTo(request.getAccessRequestLog().getTimeLimit());
        assertThat(accessRequestLog.getTimestamp()).isEqualTo(request.getAccessRequestLog().getTimestamp());
    }


}
