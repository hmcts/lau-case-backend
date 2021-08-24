package uk.gov.hmcts.reform.laubackend.cases.smoke;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = {SmokeTestConfiguration.class})
public class SmokeTests {

    @Value("${test.instance.uri}")
    private String url;

    private static final int HTTP_OK = HttpStatus.OK.value();

    RequestSpecification requestSpec;

    @BeforeEach
    public void setUp() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addParam("http.connection.timeout", "60000");
        builder.addParam("http.socket.timeout", "60000");
        builder.addParam("http.connection-manager.timeout", "60000");
        builder.setRelaxedHTTPSValidation();
        requestSpec = builder.build();
    }

    @Test
    public void shouldGetOkStatusFromHealthEndpointForLauBackend() {

        ValidatableResponse response = given().spec(requestSpec)
            .when()
            .get(url + "/health")
            .then()
            .statusCode(HTTP_OK);
        assertTrue(okResponse(response),"Health endpoint should be HTTP 200 (ok)");
    }

    @Test
    public void shouldGetOkStatusFromInfoEndpointForLauBackend() {
        ValidatableResponse response = given().spec(requestSpec)
            .when()
            .get(url + "/info")
            .then()
            .statusCode(HTTP_OK)
            .body("git.commit.id", notNullValue())
            .body("git.commit.time", notNullValue());
        assertTrue(okResponse(response), "Info endpoint should be HTTP 200 (ok)");
    }

    private boolean okResponse(ValidatableResponse response) {
        return response.extract().statusCode() == HTTP_OK ? Boolean.TRUE : Boolean.FALSE;
    }
}
