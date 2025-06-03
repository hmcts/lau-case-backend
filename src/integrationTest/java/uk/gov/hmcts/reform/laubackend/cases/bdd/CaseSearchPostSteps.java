package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.helper.RestHelper;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithInvalidUserId;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithMissingCaseRefs;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithMissingUserId;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchPostHelper.getCaseSearchPostRequestWithValidOrInvalidCaseRefs;

@SuppressWarnings({
    "PMD.UseConcurrentHashMap",
    "PMD.JUnit4TestShouldUseBeforeAnnotation",
    "PMD.TooManyMethods",
    "PMD.LawOfDemeter",
    "PMD.DoNotUseThreads",
    "PMD.SignatureDeclareThrowsException",
    "PMD.AvoidThrowingRawExceptionTypes"
})
public class CaseSearchPostSteps extends AbstractSteps {

    private final Gson jsonReader = new Gson();
    private final RestHelper restHelper = new RestHelper();
    private String caseSearchPostResponseBody;
    private int httpStatusResponseCode;

    private static final String THREAD_NAME = "threadName";
    private static final String RESPONSE = "response";

    @Before
    public void setUp() {
        setupAuthorisationStub();
    }


    @After
    public void clearScenarioContext() {
        ScenarioContext.clear();
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
                .postObject(getCaseSearchPostRequestWithInvalidUserId(), baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I request POST {string} endpoint with missing s2s header")
    public void caseSearchWithMissingAuthHeader(final String path) {
        final Response response = restHelper
                .postObjectWithoutHeader(getCaseSearchPostRequestWithInvalidUserId(), baseUrl() + path);

        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I request POST {string} endpoint using invalid caseRefs using s2s")
    public void postCaseSearchUsingInvalidCaseRefs(final String path) {

        final Response response = restHelper
                .postObject(getCaseSearchPostRequestWithValidOrInvalidCaseRefs(false), baseUrl() + path);

        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        caseSearchPostResponseBody = response.getBody().asString();
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

    @Then("caseSearch response body is returned with valid caseRefs response")
    public void caseSearchResponseBodyIsReturnedWithValidCaseRefsResponse() {
        final CaseSearchPostRequest caseSearchPostRequest = getCaseSearchPostRequestWithValidOrInvalidCaseRefs(true);
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


    @When("I request POST {string} endpoint with s2s in asynchronous mode")
    public void requestPostCaseSearchEndpointWithS2S(final String path) throws Exception {
        CompletableFuture<Response> future = CompletableFuture.supplyAsync(() -> {
            String threadName = Thread.currentThread().getName();
            ScenarioContext.set(THREAD_NAME, threadName);

            return restHelper.postObject(getCaseSearchPostRequest(), baseUrl() + path);
        });

        Response response = future.get(); // Wait for the async call to complete

        // Assert
        String threadName = ScenarioContext.get(THREAD_NAME);
        assertThat(threadName).isNotEqualTo("main"); // Verify it's not the main thread
        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());

        caseSearchPostResponseBody = response.getBody().asString();
    }

    @When("I request 10 request to POST {string} endpoint with s2s in asynchronous mode")
    public void requestMultiplePostCaseSearchEndpointWithS2S(final String path) throws Exception {
        List<CompletableFuture<Response>> futures = new ArrayList<>();
        int numRequests = 10;

        for (int i = 0; i < numRequests; i++) {
            final int idx = i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                String threadName = Thread.currentThread().getName();
                ScenarioContext.set(THREAD_NAME + idx, threadName);
                return restHelper.postObject(getCaseSearchPostRequest(), baseUrl() + path);
            }));
        }

        // Wait for all futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        for (int i = 0; i < numRequests; i++) {
            Response response = futures.get(i).get();
            ScenarioContext.set(RESPONSE + i, response);
            String threadName = ScenarioContext.get(THREAD_NAME + i);
            assertThat(threadName).isNotEqualTo("main");
            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        }
    }

    @Then("caseSearch response body is returned for all ten requests")
    public void caseSearchResponseBodyIsReturnedForAllTenRequests() {
        for (int i = 0; i < 10; i++) {
            Response response = ScenarioContext.get(RESPONSE + i);
            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        }
    }

    @When("I request 10 requests to POST {string} endpoint with s2s with simulate failures")
    public void requestMultiplePostCaseSearchEndpointWithFailures(final String path) throws Exception {
        List<CompletableFuture<Response>> futures = new ArrayList<>();
        int numRequests = 10;

        for (int i = 0; i < numRequests; i++) {
            final int idx = i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                String threadName = Thread.currentThread().getName();
                ScenarioContext.set(THREAD_NAME + idx, threadName);

                if (idx == 3  || idx == 6 || idx == 9) { // Fail every third request
                    throw new RuntimeException("Simulated failure" + idx);
                }

                return restHelper.postObject(getCaseSearchPostRequest(), baseUrl() + path);
            }).exceptionally(ex -> {
                ScenarioContext.set("error" + idx, ex.getMessage());
                return null; // Return null for failed requests
            }));
        }

        // Wait for all futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        for (int i = 0; i < 10; i++) {
            CompletableFuture<Response> future = futures.get(i);
            if (!future.isCompletedExceptionally()) {
                Response response = future.get();
                String threadName = ScenarioContext.get(THREAD_NAME + i);
                assertThat(threadName).isNotEqualTo("main");
                ScenarioContext.set(RESPONSE + i, response);
            }
        }
    }

    @Then("caseSearch response body is returned for all ten requests with some failures")
    public void caseSearchResponseBodyIsReturnedForAllTenRequestsWithFailures() {
        for (int i = 0; i < 10; i++) {
            if (ScenarioContext.get("error" + i) != null) {
                String errorMessage = ScenarioContext.get("error" + i);
                assertThat(errorMessage).contains("Simulated failure");
            } else {
                Response response = ScenarioContext.get(RESPONSE + i);
                if (response != null) {
                    assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
                }
            }
        }
    }
}
