package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static uk.gov.hmcts.reform.laubackend.cases.helper.AccessRequestHelper.getAccessRequestPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.AccessRequestHelper.mapToAccessRequestPostRequestBody;
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
    public void accessRequestResponseBodyIsReturned() throws JsonProcessingException {
        final AccessRequestPostRequest request = getAccessRequestPostRequest();

        assertResponse(request);
    }

    @When("POST to {string} endpoint with bad request body using s2s returns Bad Request")
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

    private void assertResponse(final AccessRequestPostRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        final AccessRequestPostRequest response = objectMapper.readValue(
            accessRequestPostResponseBody, AccessRequestPostRequest.class);

        assertThat(response).isNotNull();
        AccessRequestLog accessRequestLog = response.getAccessLog();
        assertThat(accessRequestLog).isNotNull();

        assertThat(accessRequestLog.getRequestType()).isEqualTo(request.getAccessLog().getRequestType());
        assertThat(accessRequestLog.getUserId()).isEqualTo(request.getAccessLog().getUserId());
        assertThat(accessRequestLog.getCaseRef()).isEqualTo(request.getAccessLog().getCaseRef());
        assertThat(accessRequestLog.getReason()).isEqualTo(request.getAccessLog().getReason());
        assertThat(accessRequestLog.getAction()).isEqualTo(request.getAccessLog().getAction());
        assertThat(accessRequestLog.getRequestStart()).isEqualTo(request.getAccessLog().getRequestStart());
        assertThat(accessRequestLog.getRequestEnd()).isEqualTo(request.getAccessLog().getRequestEnd());
        assertThat(accessRequestLog.getTimestamp()).isEqualTo(request.getAccessLog().getTimestamp());
    }


}
