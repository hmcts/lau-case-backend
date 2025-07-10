package uk.gov.hmcts.reform.laubackend.cases.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.gov.hmcts.reform.laubackend.cases.helper.RestHelper;

import java.util.Map;

import static java.lang.Integer.parseInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.bdd.WiremokInstantiator.INSTANCE;

@SuppressWarnings("PMD.LawOfDemeter")
public class CommonSteps {

    @LocalServerPort
    private int port;

    private int httpStatusResponseCode;

    final RestHelper restHelper = new RestHelper();

    private String getUrl(final String path) {
        return "http://localhost:" + port + path;
    }

    @Given("^LAU backend application is healthy$")
    public void checkApplication() {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(getUrl(""))
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when()
                .get()
                .andReturn();

        assertThat(response.getBody().asString()).contains("Welcome");
    }

    @When("And I GET {string} without service authorization header")
    public void searchCaseActionWithoutAuthHeader(final String path) {
        final Response response = restHelper.getResponseWithoutHeader(getUrl(path));
        httpStatusResponseCode = response.getStatusCode();
    }

    @When("And I GET {string} without authorization header")
    public void searchWithoutAuthorizationHeader(final String path) {
        INSTANCE.getWireMockServer().resetRequests();
        final Response response = restHelper.getResponseWithoutAuthorizationHeader(getUrl(path));
        httpStatusResponseCode = response.getStatusCode();
    }

    @When("I request GET {string} endpoint without mandatory params")
    public void requestWithoutMandatoryParams(final String path) {
        INSTANCE.getWireMockServer().resetRequests();
        final Response response = restHelper.getResponse(getUrl(path), Map.of("nonExistingParam", "nonExistingValue"));
        httpStatusResponseCode = response.getStatusCode();
    }

    @Then("HTTP {string} Forbidden response is returned")
    public void assertResponseCode(final String responseCode) {
        assertThat(httpStatusResponseCode).isEqualTo(parseInt(responseCode));
    }

    @Then("HTTP {string} Bad Request response is returned")
    public void assertErrorResponse(final String errorCode) {
        assertThat(httpStatusResponseCode).isEqualTo(parseInt(errorCode));
    }

    @Then("HTTP {string} Unauthorized response is returned")
    public void assertUnauthorizedRseponse(final String responseCode) {
        assertThat(httpStatusResponseCode).isEqualTo(parseInt(responseCode));
    }
}
