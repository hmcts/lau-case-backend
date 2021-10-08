package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SuppressWarnings({"PMD.UseConcurrentHashMap", "PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class CaseSearchSteps extends AbstractSteps {

    private String s2sToken;
    private String caseSearchPostResponseBody;

    @Given("^LAU backend application is healthy$")
    public void checkApplication() {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when()
                .get()
                .andReturn();

        assertThat(response.getBody().asString()).contains("Welcome");
    }

    @And("^Service authorisation provider is healthy")
    public void s2sSignIn() {

        setupS2SAuthorisationStub();

        final Map<String, Object> params = of(
                "microservice", getS2sName(),
                "oneTimePassword", new GoogleAuthenticator().getTotpPassword(getS2sSecret())
        );

        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(getWireMockServer().baseUrl())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/lease")
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(200);

        s2sToken = response.getBody().asString();
    }


    @When("I request POST caseSearch endpoint using s2s")
    public void postCaseAction() {

        setupServiceAuthorisationStub();

        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl() + "/audit/caseSearch")
                .body(getCaseSearchPostRequest())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + s2sToken)
                .when()
                .post()
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(201);

        caseSearchPostResponseBody = response.getBody().asString();
    }

    @Then("caseSearch response body is returned")
    public void caseSearchResponseBodyIsReturned() {
        assertThat(caseSearchPostResponseBody).contains("searchLog");
    }


    private CaseSearchPostRequest getCaseSearchPostRequest() {

        final SearchLog searchLog = new SearchLog();
        searchLog.setUserId("3748230");
        searchLog.setCaseRefs(asList("1615817621013640", "1615817621013642", "1615817621013600", "1615817621013601"));
        searchLog.setTimestamp("2021-08-23T22:20:05.023Z");

        final CaseSearchPostRequest caseSearchPostRequest = new CaseSearchPostRequest();
        caseSearchPostRequest.setSearchLog(searchLog);

        return caseSearchPostRequest;
    }
}
