package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchGetHelper.getCaseRefs;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseSearchGetHelper.getCaseSearchPostRequest;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class CaseSearchGetSteps extends AbstractSteps {

    private static final String START_TIME = "2021-07-23T22:20:05";
    private static final String END_TIME = "2021-09-23T22:20:05";

    private static final String START_TIME_PARAMETER = "startTimestamp";
    private static final String END_TIME_PARAMETER = "endTimestamp";


    private String caseSearchPostResponseBody;
    private final Gson jsonReader = new Gson();

    @Before
    public void setUp() {
        setupAuthorisationStub();
    }

    @When("I POST multiple caseSearch records to {string} endpoint using userIds {string}")
    public void postCaseActionWithUserIds(final String path, final String userIds) {
        final List<String> userIdList = asList(userIds.split(","));
        userIdList.forEach(userId -> {
            final Response response = restHelper.postObject(getCaseSearchPostRequest(userId, null, null),
                    baseUrl() + path);
            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @When("I POST multiple caseSearch records to {string} endpoint endpoint using {string} timestamp")
    public void postCaseActionWithTimestamps(final String path, final String timestamp) {
        final List<String> timestampList = asList(timestamp.split(","));
        timestampList.forEach(timestampParam -> {
            final Response response = restHelper.postObject(getCaseSearchPostRequest(null,
                            null,
                            timestampParam),
                    baseUrl() + path);

            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @When("I POST multiple caseSearch records to {string} endpoint using caseRefs {string}")
    public void postCaseActionWithCaseRefs(final String path, final String caseRefs) {
        final List<String> caseRefsList = asList(caseRefs.split(","));
        final Response response = restHelper.postObject(getCaseSearchPostRequest(null, caseRefsList, null),
                baseUrl() + path);
        assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
    }

    @And("I GET {string} using userId {string} query parameter")
    public void caseSearchUsingUserId(final String path, final String userId) {
        final Response response = restHelper.getResponse(baseUrl() + path,
                Map.of("userId", userId, START_TIME_PARAMETER, START_TIME, END_TIME_PARAMETER, END_TIME));
        caseSearchPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using startTimestamp {string} endTimestamp {string} query parameter")
    public void caseSearchUsingStartTimestamp(final String path,
                                              final String startTimestamp,
                                              final String endTimestamp) {
        final Response response = restHelper.getResponse(baseUrl() + path,
                Map.of(START_TIME_PARAMETER, startTimestamp, END_TIME_PARAMETER, endTimestamp));
        caseSearchPostResponseBody = response.getBody().asString();
    }

    @And("I GET {string} using caseRef {string} query parameter")
    public void caseSearchUsingCaseRef(final String path, final String caseRef) {
        final Response response = restHelper.getResponse(baseUrl() + path,
                Map.of("caseRef", caseRef, START_TIME_PARAMETER, START_TIME, END_TIME_PARAMETER, END_TIME));
        caseSearchPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using endTimestamp {string} query parameter")
    public void caseSearchUsingEndTimestamp(final String path, String endTimestamp) {
        final Response response = restHelper.getResponse(baseUrl() + path, Map.of(END_TIME_PARAMETER, endTimestamp));
        caseSearchPostResponseBody = response.getBody().asString();
    }

    @Then("a single caseSearch response body is returned for userId {string}")
    public void assertUserIdResponse(final String userId) {
        final CaseSearchGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseSearchPostResponseBody, CaseSearchGetResponse.class);
        final CaseSearchPostRequest caseSearchPostRequest = getCaseSearchPostRequest(userId, null, null);

        assertResponse(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseSearch response body is returned for startTimestamp {string}")
    public void assertStartTimestampResponse(final String startTimestamp) {
        final CaseSearchGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseSearchPostResponseBody, CaseSearchGetResponse.class);
        final CaseSearchPostRequest caseSearchPostRequest = getCaseSearchPostRequest(null, null, startTimestamp);

        assertResponse(caseActionGetResponse, caseSearchPostRequest);
    }


    @Then("a single caseSearch response body is returned for endTimestamp {string}")
    public void assertEndTimestampResponse(final String endTimestamp) {
        final CaseSearchGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseSearchPostResponseBody, CaseSearchGetResponse.class);
        final CaseSearchPostRequest caseSearchPostRequest = getCaseSearchPostRequest(null, null, endTimestamp);

        assertResponse(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseSearch response body is returned for caseRef {string}")
    public void assertCaseRefResponse(final String caseRef) {
        final CaseSearchGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseSearchPostResponseBody, CaseSearchGetResponse.class);
        final CaseSearchPostRequest caseSearchPostRequest = getCaseSearchPostRequest(null, getCaseRefs(caseRef), null);

        assertResponse(caseActionGetResponse, caseSearchPostRequest);
    }

    private void assertResponse(final CaseSearchGetResponse caseSearchGetResponse,
                                final CaseSearchPostRequest caseSearchPostRequest) {
        assertThat(caseSearchGetResponse.getSearchLog().size()).isEqualTo(1);
        assertThat(caseSearchGetResponse.getSearchLog().get(0).getUserId())
                .isEqualTo(caseSearchPostRequest.getSearchLog().getUserId());
        assertThat(caseSearchGetResponse.getSearchLog().get(0).getTimestamp())
                .isEqualTo(caseSearchPostRequest.getSearchLog().getTimestamp());
        assertThat(caseSearchGetResponse.getSearchLog().get(0).getCaseRefs().get(0))
                .isEqualTo(caseSearchPostRequest.getSearchLog().getCaseRefs().get(0));
        assertThat(caseSearchGetResponse.getSearchLog().get(0).getCaseRefs().get(1))
                .isEqualTo(caseSearchPostRequest.getSearchLog().getCaseRefs().get(1));
        assertThat(caseSearchGetResponse.getSearchLog().get(0).getCaseRefs().get(2))
                .isEqualTo(caseSearchPostRequest.getSearchLog().getCaseRefs().get(2));
    }
}
