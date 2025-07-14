package uk.gov.hmcts.reform.laubackend.cases.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionPostHelper.getCaseActionPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.END_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.END_TIME_PARAMETER;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.START_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.START_TIME_PARAMETER;

@SuppressWarnings({"PMD.LawOfDemeter"})
public class CaseActionDeleteSteps extends AbstractSteps {

    private int httpStatusResponseCode;
    private String caseActionResponseBody;
    private String caseActionId;
    private static final String CASE_ACTION_ID_PARAM_NAME = "caseActionId";

    @Before
    public void setUp() {
        setupAuthorisationStub();
    }

    @When("I request POST {string} caseAction endpoint using userId {string}")
    public void postCaseAction(final String path, final String userId) {
        final Response response = restHelper.postObject(getCaseActionPostRequest(userId), baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        final CaseActionPostResponse caseActionPostResponse = jsonReader
                .fromJson(response.getBody().asString(), CaseActionPostResponse.class);

        caseActionId = caseActionPostResponse.getActionLog().getCaseActionId();

        assertThat(caseActionId).isNotEqualTo(null);
    }

    @And("I request DELETE {string} caseAction endpoint")
    public void deleteCaseAction(final String path) {
        final Response response = restHelper.deleteResponse(
            baseUrl() + path,
            CASE_ACTION_ID_PARAM_NAME,
            caseActionId);
        assertThat(response.getStatusCode()).isEqualTo(OK.value());
    }

    @And("I GET {string} caseAction using userId {string}")
    public void searchByCaseActionId(final String path, final String userId) {
        final Response response = restHelper.getResponse(baseUrl() + path, Map.of("userId", userId,
                START_TIME_PARAMETER, START_TIME,
                END_TIME_PARAMETER, END_TIME));
        caseActionResponseBody = response.getBody().asString();
    }

    @And("I request DELETE {string} caseAction endpoint with missing s2s header")
    public void deleteCaseActionWithMissingStoSHeader(final String path) {
        final Response response = restHelper.deleteResponseWithoutServiceAuthHeader(baseUrl() + path);
        httpStatusResponseCode = response.getStatusCode();
    }

    @And("I request DELETE {string} caseAction endpoint with missing authorization header")
    public void deleteCaseActionWithMissingAuthHeader(final String path) {
        final Response response = restHelper.deleteResponseWithoutAuthHeader(baseUrl() + path);
        httpStatusResponseCode = response.getStatusCode();
    }


    @And("I request DELETE {string} caseAction endpoint with invalid caseActionId {string}")
    public void deleteCaseActionWithInvalidcaseActionId(final String path, final String caseActionId) {
        final Response response = restHelper.deleteResponse(
            baseUrl() + path,
            CASE_ACTION_ID_PARAM_NAME,
            caseActionId);
        httpStatusResponseCode = response.getStatusCode();
    }

    @And("I request DELETE {string} caseAction endpoint with missing caseActionId")
    public void deleteCaseActionWithMissingcaseActionId(final String path) {
        final Response response = restHelper.deleteResponse(
            baseUrl() + path,
            CASE_ACTION_ID_PARAM_NAME,
            null);
        httpStatusResponseCode = response.getStatusCode();

    }

    @Then("http {string} response is returned for DELETE caseAction")
    public void assertHttpResponse(final String httpResponse) {
        assertThat(httpStatusResponseCode).isEqualTo(Integer.parseInt(httpResponse));
    }

    @Then("An empty result for DELETE caseAction is returned")
    public void assertResponse() {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionResponseBody, CaseActionGetResponse.class);

        assertThat(caseActionGetResponse.getActionLog().size()).isEqualTo(0);
    }

}
