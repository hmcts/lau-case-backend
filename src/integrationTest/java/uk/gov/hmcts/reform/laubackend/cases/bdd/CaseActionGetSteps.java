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
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_AUDIT_INVESTIGATOR_ROLE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_SERVICE_LOGS_ROLE;
import static uk.gov.hmcts.reform.laubackend.cases.helper.CaseActionGetHelper.getCaseActionPostRequest;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.END_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.END_TIME_PARAMETER;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.START_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.helper.RestConstants.START_TIME_PARAMETER;

@SuppressWarnings({
    "PMD.TooManyMethods",
    "PMD.JUnit4TestShouldUseBeforeAnnotation",
    "PMD.UseObjectForClearerAPI",
    "PMD.LawOfDemeter"})
public class CaseActionGetSteps extends AbstractSteps {

    private String caseActionPostResponseBody;
    private final Gson jsonReader = new Gson();

    @Before
    public void setUp() {
        setupAuthorisationStub();
    }

    @When("I POST multiple records to {string} endpoint using {string} userIds")
    public void postCaseActionForUserIds(final String path, final String pathParam) {
        final List<String> pathParams = asList(pathParam.split(","));
        pathParams.forEach(userId -> {
            final Response response = restHelper.postObject(getCaseActionPostRequest(userId,
                            null,
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
                            null,
                            null),
                    baseUrl() + path);

            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @When("I POST multiple records to {string} endpoint using {string} caseAction")
    public void postCaseAction(final String path, final String caseAction) {
        final List<String> caseActionList = asList(caseAction.split(","));
        caseActionList.forEach(caseActionParam -> {
            final Response response = restHelper.postObject(getCaseActionPostRequest(null,
                             null,
                             null,
                             null,
                              caseActionParam,
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
                            null,
                            timestampParam),
                    baseUrl() + path);

            assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
        });
    }

    @And("I am logged in with the CFT-SERVICE-LOGS role")
    public void updateUserRoleServiceLogs() {
        setupAuthorisationStubWithRole(AUTHORISATION_SERVICE_LOGS_ROLE);
    }

    @And("I am logged in with the CFT-AUDIT-INVESTIGATOR role")
    public void updateUserRoleAuditInvestigator() {
        setupAuthorisationStubWithRole(AUTHORISATION_AUDIT_INVESTIGATOR_ROLE);
    }

    @And("I GET {string} using userId {string} query param")
    public void searchUsingUserId(final String path, String userId) {
        final Response response = restHelper.getResponse(baseUrl() + path, Map.of("userId", userId,
                START_TIME_PARAMETER, START_TIME, END_TIME_PARAMETER, END_TIME));
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("I GET {string} using caseRef query param")
    public void searchUsingCaseRef(final String path) {
        Response response = restHelper.getResponse(baseUrl() + path, Map.of("caseRef", "1",
                START_TIME_PARAMETER, START_TIME, END_TIME_PARAMETER, END_TIME));
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using caseRef {string} query param")
    public void retrieveCaseActionCaseRefResponse(final String path, final String caseRef) {
        Response response = restHelper.getResponse(baseUrl() + path, Map.of("caseRef", caseRef,
                START_TIME_PARAMETER, START_TIME, END_TIME_PARAMETER, END_TIME));
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using caseTypeId {string} query param")
    public void retrieveCaseActionForCaseType(final String path, final String caseTypeId) {
        Response response = restHelper.getResponse(baseUrl() + path, Map.of("caseTypeId", caseTypeId,
                START_TIME_PARAMETER, START_TIME, END_TIME_PARAMETER, END_TIME));
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using caseAction {string} query param")
    public void retrieveCaseActionForCaseAction(final String path, final String caseAction) {
        Response response = restHelper.getResponse(baseUrl() + path, Map.of("caseAction", caseAction,
                START_TIME_PARAMETER, START_TIME, END_TIME_PARAMETER, END_TIME));
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using caseJurisdictionId {string} query param")
    public void retrieveCaseJurisdiction(final String path, final String caseJurisdictionId) {
        Response response = restHelper.getResponse(baseUrl() + path, Map.of("caseJurisdictionId", caseJurisdictionId,
                START_TIME_PARAMETER, START_TIME, END_TIME_PARAMETER, END_TIME));
        caseActionPostResponseBody = response.getBody().asString();
    }

    @And("And I GET {string} using startTimestamp {string} endTimestamp {string} caseRef {string} query param")
    public void retrieveWithStartEndTimestamp(final String path,
                                         final String startTimestamp,
                                         final String endTimestamp,
                                         final String caseRef) {
        final Response response = restHelper.getResponse(baseUrl() + path, Map.of(
                "startTimestamp",
                startTimestamp,
                "endTimestamp",
                endTimestamp,
                "caseRef",
                caseRef));
        caseActionPostResponseBody = response.getBody().asString();
    }

    @Then("a single caseAction response body is returned for userId {string}")
    public void assertResponse(final String userId) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest
            = getCaseActionPostRequest(userId, null, null, null, null,null);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseAction response body is returned for caseRef {string}")
    public void assertCaseRefResponse(final String caseRef) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
                .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest
            = getCaseActionPostRequest(null, caseRef, null, null,null, null);

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
                null,
                null);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("a single caseAction response body is returned for caseAction {string}")
    public void assertCaseAction(final String caseAction) {
        final CaseActionGetResponse caseActionGetResponse = jsonReader
            .fromJson(caseActionPostResponseBody, CaseActionGetResponse.class);
        final CaseActionPostRequest caseSearchPostRequest = getCaseActionPostRequest(null,
                 null,
                 null,
                 null,
                 caseAction,
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
                null,
                endTimestamp);

        assertObject(caseActionGetResponse, caseSearchPostRequest);
    }

    @Then("HTTP {string} Unauthorized response is returned for get request")
    public void assertUnauthorizedRseponseForGetRequest(final String responseCode) {
        assertThat(caseActionPostResponseBody).containsIgnoringCase(responseCode);
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
