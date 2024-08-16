package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostDeleteActionRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequestWithInvalidParameter;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequestWithMissingJurisdiction;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequestWithMissingMandatoryParameter;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequestWithValidOrInvalidCaseRefParameter;

@SuppressWarnings({"PMD.UseConcurrentHashMap", "PMD.JUnit4TestShouldUseBeforeAnnotation", "PMD.TooManyMethods"})
public class CaseActionPostSteps extends AbstractSteps {

    private final Gson jsonReader = new Gson();
    private String caseActionPostResponseBody;
    private int httpStatusResponseCode;

    @Before
    public void setUp() {
        setupAuthorisationStub();
    }

    @When("I POST case action using {string} endpoint using s2s")
    public void postCaseAction(final String path) {

        final Response response = restHelper.postObject(getCaseActionPostRequest(), baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        caseActionPostResponseBody = response.getBody().asString();
    }

    @When("I POST case action using {string} endpoint using case action delete")
    public void postCaseActionWithDeleteAction(final String path) {

        final Response response = restHelper.postObject(getCaseActionPostDeleteActionRequest(), baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        caseActionPostResponseBody = response.getBody().asString();
    }

    @When("I POST case action using {string} endpoint without case jurisdiction")
    public void postWithoutCaseJurisdiction(String path) {
        final Response response = restHelper
                .postObject(getCaseActionPostRequestWithMissingJurisdiction(), baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        caseActionPostResponseBody = response.getBody().asString();
    }

    @When("I POST {string} endpoint with missing request body parameter using s2s")
    public void postCaseActionWithMissingParameter(String path) {
        final Response response = restHelper.postObject(getCaseActionPostRequestWithMissingMandatoryParameter(),
                baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I POST {string} endpoint with missing invalid body parameter using s2s")
    public void caseActionWithInvalidBodyParameter(String path) {
        final Response response = restHelper.postObject(getCaseActionPostRequestWithInvalidParameter(),
                baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I POST {string} endpoint with missing s2s header")
    public void postCaseActionWithMissingAuthHeader(String path) {
        final Response response = restHelper
                .postObjectWithoutHeader(getCaseActionPostRequestWithInvalidParameter(), baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I POST case action using {string} endpoint using invalid caseRef using s2s")
    public void postCaseActionUsingEndpointUsingInvalidCaseRef(final String path) {
        final Response response = restHelper.postObject(
                getCaseActionPostRequestWithValidOrInvalidCaseRefParameter(false),
                baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        caseActionPostResponseBody = response.getBody().asString();
    }

    @Then("caseAction response body is returned")
    public void caseSearchResponseBodyIsReturned() {
        final CaseActionPostRequest caseActionPostRequest = getCaseActionPostRequest();

        assertResponse(caseActionPostRequest);
    }

    @Then("caseAction response body is returned as a delete action")
    public void caseSearchResponseBodyDeleteIsReturned() {
        final CaseActionPostRequest caseActionPostRequest = getCaseActionPostDeleteActionRequest();

        assertResponse(caseActionPostRequest);
    }

    @Then("caseAction response body is returned with missing jurisdiction")
    public void caseSearchResponseBodyIsReturnedWithMissingJurisdiction() {
        final CaseActionPostRequest caseActionPostRequest = getCaseActionPostRequestWithMissingJurisdiction();

        assertResponse(caseActionPostRequest);
    }

    @Then("caseAction response body is returned with valid caseRef response")
    public void caseActionResponseBodyIsReturnedWithValidCaseRefResponse() {
        final CaseActionPostRequest caseActionPostRequest =
                getCaseActionPostRequestWithValidOrInvalidCaseRefParameter(true);

        assertResponse(caseActionPostRequest);
    }

    @Then("http forbidden response is returned for POST caseAction")
    public void unauthorisedResponseReturned() {
        assertThat(httpStatusResponseCode).isEqualTo(FORBIDDEN.value());
    }

    @Then("http bad request response is returned for POST caseAction")
    public void badRequestResponseBodyIsReturned() {
        assertThat(httpStatusResponseCode).isEqualTo(BAD_REQUEST.value());
    }

    private void assertResponse(final CaseActionPostRequest caseActionPostRequest) {
        final CaseActionPostResponse caseActionPostResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionPostResponse.class);

        assertThat(caseActionPostResponse.getActionLog().getCaseActionId()).isNotNull();
        assertThat(caseActionPostRequest.getActionLog().getUserId())
                .isEqualTo(caseActionPostResponse.getActionLog().getUserId());
        assertThat(caseActionPostRequest.getActionLog().getTimestamp())
                .isEqualTo(caseActionPostResponse.getActionLog().getTimestamp());
        assertThat(caseActionPostRequest.getActionLog().getCaseAction())
                .isEqualTo(caseActionPostResponse.getActionLog().getCaseAction());
        assertThat(caseActionPostRequest.getActionLog().getCaseRef())
                .isEqualTo(caseActionPostResponse.getActionLog().getCaseRef());
        assertThat(caseActionPostRequest.getActionLog().getCaseTypeId())
                .isEqualTo(caseActionPostResponse.getActionLog().getCaseTypeId());
        assertThat(caseActionPostRequest.getActionLog().getCaseJurisdictionId())
                .isEqualTo(caseActionPostResponse.getActionLog().getCaseJurisdictionId());
    }
}
