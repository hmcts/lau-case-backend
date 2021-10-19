package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionHelper.getCaseActionPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionHelper.getCaseActionPostRequestWithInvalidParameter;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionHelper.getCaseActionPostRequestWithMissingMandatoryParameter;

@SuppressWarnings({"PMD.UseConcurrentHashMap", "PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class CaseActionPostSteps extends AbstractSteps {

    private String caseActionPostResponseBody;
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


    @When("I POST case action using {string} endpoint using s2s")
    public void postCaseAction(final String path) {

        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + path)
                .body(getCaseActionPostRequest())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .post()
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        caseActionPostResponseBody = response.getBody().asString();
    }

    @When("I POST {string} endpoint with missing request body parameter using s2s")
    public void postCaseActionWithMissingParameter(String path) {

        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + path)
                .body(getCaseActionPostRequestWithMissingMandatoryParameter())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .post()
                .andReturn();

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I POST {string} endpoint with missing invalid body parameter using s2s")
    public void caseActionWithInvalidBodyParameter(String path) {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + path)
                .body(getCaseActionPostRequestWithInvalidParameter())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + AUTH_TOKEN)
                .when()
                .post()
                .andReturn();

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I POST {string} endpoint with missing s2s header")
    public void postCaseActionWithMissingAuthHeader(String path) {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + path)
                .body(getCaseActionPostRequestWithInvalidParameter())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when()
                .post()
                .andReturn();

        httpStatusResponseCode = response.getStatusCode();
    }

    @Then("caseAction response body is returned")
    public void caseSearchResponseBodyIsReturned() {
        final CaseActionPostRequest caseActionPostRequest = getCaseActionPostRequest();
        final CaseActionPostResponse caseActionPostResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionPostResponse.class);

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

    @Then("http unauthorised response is returned for POST caseAction")
    public void unauthorisedResponseReturned() {
        assertThat(httpStatusResponseCode).isEqualTo(UNAUTHORIZED.value());
    }

    @Then("http bad request response is returned for POST caseAction")
    public void badRequestResponseBodyIsReturned() {
        assertThat(httpStatusResponseCode).isEqualTo(BAD_REQUEST.value());
    }
}
