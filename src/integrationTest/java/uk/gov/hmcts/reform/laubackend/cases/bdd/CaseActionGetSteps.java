package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionGetResponse;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionGetHelper.getCaseActionPostRequest;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class CaseActionGetSteps extends AbstractSteps {

    private String caseActionPostResponseBody;
    private final Gson jsonReader = new Gson();

    @Before
    public void setUp() {
        setupServiceAuthorisationStub();
    }

    @When("I POST multiple records to {string} endpoint using {string} userIds")
    public void postCaseActionForUserIds(final String path, final String pathParam) {
        final List<String> pathParams = asList(pathParam.split(","));
        pathParams.forEach(userId -> {
            final Response response = restHelper.postObject(getCaseActionPostRequest(userId,
                            null,
                            null,
                            null,
                            null),
                    baseUrl() + path);

            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @When("I POST multiple records to {string} endpoint using {string} caseRefs")
    public void postCaseActionForCaseRefs(final String path, final String caseRefs) {
        final List<String> caseRefsList = asList(caseRefs.split(","));
        caseRefsList.forEach(caseRef -> {
            final Response response = restHelper.postObject(getCaseActionPostRequest(null,
                            caseRef,
                            null,
                            null,
                            null),
                    baseUrl() + path);

            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @When("I POST multiple records to {string} endpoint using {string} caseJurisdictionId")
    public void postCaseJurisdiction(final String path, final String caseJurisdiction) {
        final List<String> caseJurisdictionList = asList(caseJurisdiction.split(","));
        caseJurisdictionList.forEach(caseJurisdictionParam -> {
            final Response response = restHelper.postObject(getCaseActionPostRequest(null,
                            null,
                            caseJurisdictionParam,
                            null,
                            null),
                    baseUrl() + path);

            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @When("I POST multiple records to {string} endpoint using {string} caseTypeId")
    public void postWithCaseAction(final String path, final String caseTypeId) {
        final List<String> caseTypeIdList = asList(caseTypeId.split(","));
        caseTypeIdList.forEach(caseTypeIdParam -> {
            final Response response = restHelper.postObject(getCaseActionPostRequest(null,
                            null,
                            null,
                            caseTypeIdParam,
                            null),
                    baseUrl() + path);

            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @When("I POST multiple records to {string} endpoint using {string} timestamp")
    public void postWithTimestamp(final String path, final String timestamp) {
        final List<String> pathParams = asList(timestamp.split(","));
        pathParams.forEach(timestampParam -> {
            final Response response = restHelper.postObject(getCaseActionPostRequest(null,
                            null,
                            null,
                            null,
                            timestampParam),
                    baseUrl() + path);

            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @And("I GET {string} using userId {string} query param")
    public void searchUsingUserId(final String path, String userId) {
        final Response response = restHelper.getResponse(baseUrl() + path, "userId", userId);
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("I GET {string} using caseRef query param")
    public void searchUsingCaseRef(final String path) {
        Response response = restHelper.getResponse(baseUrl() + path, "caseRef", "1");
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using caseRef {string} query param")
    public void retrieveCaseActionCaseRefResponse(final String path, final String caseRef) {
        Response response = restHelper.getResponse(baseUrl() + path, "caseRef", caseRef);
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using caseTypeId {string} query param")
    public void retrieveCaseActionForCaseType(final String path, final String caseTypeId) {
        Response response = restHelper.getResponse(baseUrl() + path, "caseTypeId", caseTypeId);
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using caseJurisdictionId {string} query param")
    public void retrieveCaseJurisdiction(final String path, final String caseJurisdictionId) {
        Response response = restHelper.getResponse(baseUrl() + path, "caseJurisdictionId", caseJurisdictionId);
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using endTimestamp {string} query param")
    public void retrieveWithEndTimestamp(final String path, final String endTimestamp) {
        final Response response = restHelper.getResponse(baseUrl() + path, "endTimestamp", endTimestamp);
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using startTimestamp {string} query param")
    public void retrieveWithStartTime(final String path, final String startTimestamp) {
        final Response response = restHelper.getResponse(baseUrl() + path, "startTimestamp", startTimestamp);
        caseActionPostResponseBody = response.getBody().asString();
    }

    @Then("a single caseAction response body is returned for userId {string}")
    public void assertResponse(final String userId) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest = getCaseActionPostRequest(userId, null, null, null, null);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseAction response body is returned for caseRef {string}")
    public void assertCaseRefResponse(final String caseRef) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest = getCaseActionPostRequest(null, caseRef, null, null, null);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseAction response body is returned for caseTypeId {string}")
    public void retrieveCaseActionResponse(final String caseTypeId) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest = getCaseActionPostRequest(null,
                null,
                null,
                caseTypeId,
                null);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseAction response body is returned for caseJurisdictionId {string}")
    public void assertCaseJurisdiction(final String caseJurisdictionId) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest = getCaseActionPostRequest(null,
                null, caseJurisdictionId,
                null,
                null);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseAction response body is returned for startTimestamp {string}")
    public void assertStartTimeResponse(final String startTimestamp) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest = getCaseActionPostRequest(null,
                null,
                null,
                null,
                startTimestamp);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseAction response body is returned for endTimestamp {string}")
    public void assertWithEndTimestamp(final String endTimestamp) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest = getCaseActionPostRequest(null,
                null,
                null,
                null,
                endTimestamp);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    private void assertObject(final CaseActionGetResponse caseActionGetResponse,
                              final CaseActionPostRequest caseSearchPostRequest) {
        assertThat(caseActionGetResponse.getActionLog().size()).isEqualTo(1);
        assertThat(caseActionGetResponse.getActionLog().get(0).getCaseJurisdictionId())
                .isEqualTo(caseSearchPostRequest.getActionLog().getCaseJurisdictionId());
        assertThat(caseActionGetResponse.getActionLog().get(0).getCaseAction())
                .isEqualTo(caseSearchPostRequest.getActionLog().getCaseAction());
        assertThat(caseActionGetResponse.getActionLog().get(0).getCaseRef())
                .isEqualTo(caseSearchPostRequest.getActionLog().getCaseRef());
        assertThat(caseActionGetResponse.getActionLog().get(0).getCaseTypeId())
                .isEqualTo(caseSearchPostRequest.getActionLog().getCaseTypeId());
    }


}
