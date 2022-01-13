package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequest;

@SuppressWarnings({"PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class CaseSearchDeleteSteps extends AbstractSteps {

    private int httpStatusResponseCode;
    private String caseSearchResponseBody;
    private final Gson jsonReader = new Gson();
    private String caseSearchId;

    @Before
    public void setUp() {
        setupServiceAuthorisationStub();
    }

    @When("I request POST {string} endpoint using userId {string}")
    public void postCaseAction(final String path, final String userId) {
        final Response response = restHelper.postObject(getCaseSearchPostRequest(userId), baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        final CaseSearchPostResponse caseSearchPostResponse = jsonReader
                .fromJson(response.getBody().asString(), CaseSearchPostResponse.class);

        caseSearchId = caseSearchPostResponse.getSearchLog().getId();

        assertThat(caseSearchId).isNotEqualTo(null);
    }

    @And("I request DELETE {string} endpoint")
    public void deleteCaseSearch(final String path) {
        final Response response = restHelper.deleteResponse(baseUrl() + path, "caseSearchId", caseSearchId);
        assertThat(response.getStatusCode()).isEqualTo(OK.value());
    }

    @And("I GET {string} using userId {string}")
    public void searchByCaseSearchId(final String path, final String userId) {
        final Response response = restHelper.getResponse(baseUrl() + path, "userId", userId);
        caseSearchResponseBody = response.getBody().asString();
    }

    @And("I request DELETE {string} endpoint with missing s2s header")
    public void deleteCaseSearchWithMissingStoSHeader(final String path) {
        final Response response = restHelper.deleteResponseWithoutServiceAuthHeader(baseUrl() + path);
        httpStatusResponseCode = response.getStatusCode();
    }

    @And("I request DELETE {string} endpoint with missing authorization header")
    public void deleteCaseSearchWithMissingAuthHeader(final String path) {
        final Response response = restHelper.deleteResponseWithoutAuthHeader(baseUrl() + path);
        httpStatusResponseCode = response.getStatusCode();
    }


    @And("I request DELETE {string} endpoint with invalid caseSearchId {string}")
    public void deleteCaseSearchWithInvalidCaseSearchId(final String path, final String caseSearchId) {
        final Response response = restHelper.deleteResponse(baseUrl() + path, "caseSearchId", caseSearchId);
        httpStatusResponseCode = response.getStatusCode();
    }

    @And("I request DELETE {string} endpoint with missing caseSearchId")
    public void deleteCaseSearchWithMissingCaseSearchId(final String path) {
        final Response response = restHelper.deleteResponse(baseUrl() + path, "caseSearchId", null);
        httpStatusResponseCode = response.getStatusCode();

    }

    @Then("http {string} response is returned for DELETE caseSearch")
    public void assertHttpResponse(final String httpResponse) {
        assertThat(Integer.parseInt(httpResponse)).isEqualTo(httpStatusResponseCode);
    }

    @Then("An empty result is returned")
    public void assertResponse() {
        final CaseSearchGetResponse caseSearchGetResponse = jsonReader
                .fromJson(caseSearchResponseBody, CaseSearchGetResponse.class);

        assertThat(caseSearchGetResponse.getSearchLog().size()).isEqualTo(0);
    }

}
