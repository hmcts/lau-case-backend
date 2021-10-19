package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithInvalidCaseRefs;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithMissingCaseRefs;

@SuppressWarnings({"PMD.UseConcurrentHashMap", "PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class CaseSearchPostSteps extends AbstractSteps {

    private String caseSearchPostResponseBody;
    private int httpStatusResponseCode;
    private final Gson jsonReader = new Gson();

    @Before
    public void setUp() throws InterruptedException {
        wiremokInstantiator.startWiremock();
        setupServiceAuthorisationStub();
        SECONDS.sleep(1);
    }

    @After
    public void shutDown() throws InterruptedException {
        wiremokInstantiator.stopWiremock();
        SECONDS.sleep(1);
    }

    @When("I request POST {string} endpoint using s2s")
    public void postCaseAction(final String path) {

        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + path)
                .body(getCaseSearchPostRequest())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .post()
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        caseSearchPostResponseBody = response.getBody().asString();
    }

    @When("I request POST {string} endpoint with missing request body parameter using s2s")
    public void caseSearchWithMissingBodyParameter(final String path) {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + path)
                .body(getCaseSearchPostRequestWithMissingCaseRefs())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .post()
                .andReturn();
        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I request POST {string} endpoint with invalid body parameter using s2s")
    public void caseSearchWithInvalidBodyParameter(final String path) {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + path)
                .body(getCaseSearchPostRequestWithInvalidCaseRefs())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .post()
                .andReturn();
        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I request POST {string} endpoint with missing s2s header")
    public void caseSearchWithMissingAuthHeader(final String path) {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + path)
                .body(getCaseSearchPostRequestWithInvalidCaseRefs())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when()
                .post()
                .andReturn();
        httpStatusResponseCode = response.getStatusCode();
    }

    @Then("unauthorised response is returned")
    public void unauthorisedResponseReturned() {
        assertThat(httpStatusResponseCode).isEqualTo(UNAUTHORIZED.value());
    }

    @Then("http bad request response is returned")
    public void badRequestResponseBodyIsReturned() {
        assertThat(httpStatusResponseCode).isEqualTo(BAD_REQUEST.value());
    }

    @Then("caseSearch response body is returned")
    public void caseSearchResponseBodyIsReturned() {
        final CaseSearchPostRequest caseSearchPostRequest = getCaseSearchPostRequest();
        final CaseSearchPostRequest caseSearchPostResponse = jsonReader
                .fromJson(caseSearchPostResponseBody, CaseSearchPostRequest.class);

        assertThat(caseSearchPostRequest.getSearchLog().getUserId())
                .isEqualTo(caseSearchPostResponse.getSearchLog().getUserId());
        assertThat(caseSearchPostRequest.getSearchLog().getTimestamp())
                .isEqualTo(caseSearchPostResponse.getSearchLog().getTimestamp());
        assertThat(caseSearchPostRequest.getSearchLog().getCaseRefs().get(0))
                .isEqualTo(caseSearchPostResponse.getSearchLog().getCaseRefs().get(0));
        assertThat(caseSearchPostRequest.getSearchLog().getCaseRefs().get(1))
                .isEqualTo(caseSearchPostResponse.getSearchLog().getCaseRefs().get(1));
        assertThat(caseSearchPostRequest.getSearchLog().getCaseRefs().get(2))
                .isEqualTo(caseSearchPostResponse.getSearchLog().getCaseRefs().get(2));
        assertThat(caseSearchPostRequest.getSearchLog().getCaseRefs().get(3))
                .isEqualTo(caseSearchPostResponse.getSearchLog().getCaseRefs().get(3));
    }

}
