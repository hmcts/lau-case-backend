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
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequestWithInvalidParameter;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequestWithMissingJurisdiction;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequestWithMissingMandatoryParameter;

@SuppressWarnings({"PMD.UseConcurrentHashMap", "PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class CaseActionPostSteps extends AbstractSteps {

    private String caseActionPostResponseBody;
    private int httpStatusResponseCode;
    private final Gson jsonReader = new Gson();

    @Before
    public void setUp() {
        setupServiceAuthorisationStub();
    }

    @When("I POST case action using {string} endpoint using s2s")
    public void postCaseAction(final String path) {

        final Response response = restHelper.postObject(getCaseActionPostRequest(), baseUrl() + path);

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

    @Then("caseAction response body is returned")
    public void caseSearchResponseBodyIsReturned() {
        final CaseActionPostRequest caseActionPostRequest = getCaseActionPostRequest();

        assertResponse(caseActionPostRequest);
    }

    @Then("caseAction response body is returned with missing jurisdiction")
    public void caseSearchResponseBodyIsReturnedWithMissingJurisdiction() {
        final CaseActionPostRequest caseActionPostRequest = getCaseActionPostRequestWithMissingJurisdiction();

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

        assertThat(caseActionPostResponse.getActionLog().getCaseActionId())
                .isNotEqualTo(null);
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
