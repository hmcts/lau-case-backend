package uk.gov.hmcts.reform.laubackend.cases.bdd;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CommonSteps {

    @LocalServerPort
    private int port;

    @Given("^LAU backend application is healthy$")
    public void checkApplication() {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri("http://localhost:" + port)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when()
                .get()
                .andReturn();

        assertThat(response.getBody().asString()).contains("Welcome");
    }
}
