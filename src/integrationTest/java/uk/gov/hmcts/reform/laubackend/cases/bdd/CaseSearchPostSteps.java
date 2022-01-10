package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.helper.RestHelper;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithInvalidCaseRefs;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithMissingCaseRefs;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithMissingUserId;

@SuppressWarnings({"PMD.UseConcurrentHashMap", "PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class CaseSearchPostSteps extends AbstractSteps {

    private String caseSearchPostResponseBody;
    private int httpStatusResponseCode;
    private final Gson jsonReader = new Gson();
    private final RestHelper restHelper = new RestHelper();

    @Before
    public void setUp() {
        setupServiceAuthorisationStub();
    }

    @When("I request POST {string} endpoint using s2s")
    public void postCaseAction(final String path) {

        final Response response = restHelper
                .postObject(getCaseSearchPostRequest(), baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        caseSearchPostResponseBody = response.getBody().asString();
    }

    @When("I request POST {string} endpoint using s2s and missing caseRefs")
    public void caseSearchWithMissingCaseRefs(String path) {
        final Response response = restHelper
                .postObject(getCaseSearchPostRequestWithMissingCaseRefs(), baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        caseSearchPostResponseBody = response.getBody().asString();
    }

    @When("I request POST {string} endpoint with missing request body parameter using s2s")
    public void caseSearchWithMissingBodyParameter(final String path) {

        final Response response = restHelper
                .postObject(getCaseSearchPostRequestWithMissingUserId(), baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I request POST {string} endpoint with invalid body parameter using s2s")
    public void caseSearchWithInvalidBodyParameter(final String path) {

        final Response response = restHelper
                .postObject(getCaseSearchPostRequestWithInvalidCaseRefs(), baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I request POST {string} endpoint with missing s2s header")
    public void caseSearchWithMissingAuthHeader(final String path) {
        final Response response = restHelper
                .postObjectWithoutHeader(getCaseSearchPostRequestWithInvalidCaseRefs(), baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }

    @Then("http forbidden response is returned for POST caseSearch")
    public void unauthorisedResponseReturned() {
        assertThat(httpStatusResponseCode).isEqualTo(FORBIDDEN.value());
    }

    @Then("http bad request response is returned")
    public void badRequestResponseBodyIsReturned() {
        assertThat(httpStatusResponseCode).isEqualTo(BAD_REQUEST.value());
    }

    @Then("caseSearch response body is returned")
    public void caseSearchResponseBodyIsReturned() {
        final CaseSearchPostRequest caseSearchPostRequest = getCaseSearchPostRequest();
        final CaseSearchPostResponse caseSearchPostResponse = jsonReader
                .fromJson(caseSearchPostResponseBody, CaseSearchPostResponse.class);
        assertResponseBody(caseSearchPostRequest, caseSearchPostResponse);
    }

    @Then("caseSearch response body with missing caseRefs is returned")
    public void caseSearchResponseBodyWithMissingCaseRefsIsReturned() {
        final CaseSearchPostRequest caseSearchPostRequest = getCaseSearchPostRequestWithMissingCaseRefs();
        final CaseSearchPostResponse caseSearchPostResponse = jsonReader
                .fromJson(caseSearchPostResponseBody, CaseSearchPostResponse.class);
        assertResponseBody(caseSearchPostRequest, caseSearchPostResponse);
    }

    private void assertResponseBody(final CaseSearchPostRequest caseSearchPostRequest,
                                    final CaseSearchPostResponse caseSearchPostResponse) {

        assertThat(caseSearchPostResponse.getSearchLog().getId()).isNotEqualTo(null);
        assertThat(caseSearchPostRequest.getSearchLog().getUserId())
                .isEqualTo(caseSearchPostResponse.getSearchLog().getUserId());
        assertThat(caseSearchPostRequest.getSearchLog().getTimestamp())
                .isEqualTo(caseSearchPostResponse.getSearchLog().getTimestamp());
        if (!isEmpty(caseSearchPostRequest.getSearchLog().getCaseRefs())) {
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
}
