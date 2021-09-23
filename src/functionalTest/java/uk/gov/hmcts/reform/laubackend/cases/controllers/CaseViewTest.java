package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.laubackend.cases.client.IdamServiceClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@Configuration
@SpringBootTest
@ComponentScan("uk.gov.hmcts.reform.laubackend.cases")
@ActiveProfiles("functional")
@Slf4j
public class CaseViewTest {

    public static final String SERVICE_AUTHORISATION_HEADER = "ServiceAuthorization";

    @Value("${targetInstance}")
    private String lauCaseBackEndApiUrl;

    @Value("${idam.s2s-auth.name-cs}")
    private String s2sName;

    @Value("${idam.s2s-auth.secret-cs}")
    private String s2sSecret;

    @Value("${idam.s2s-auth.url}")
    private String s2sUrl;

    private IdamServiceClient idamServiceClient;

    @Before
    public void setUp() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.defaultParser = Parser.JSON;
        idamServiceClient = new IdamServiceClient(s2sName, s2sSecret, s2sUrl);
    }

    @Test
    @Ignore("This test has to be extended. We will need to add more assertions "
            + "and cover some other cases when functional test ticket is started")
    public void shouldGetCaseViewWithS2SToken() {
        try {
            final String s2sString = idamServiceClient.s2sSignIn();
            assertNotNull("S2S String should contain a value", s2sString);
            final String caseView = fetchCaseViewResponse(s2sString);

            assertThat(caseView).contains("viewLog");

        } catch (final Exception e) {
            fail("Unable to successfully retrieve SAS token: " + e.getMessage());
        }
    }


    public String fetchCaseViewResponse(final String serviceAuthentication) {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(lauCaseBackEndApiUrl + "/audit/caseView")
                .queryParam("startTimestamp", "2000-08-23T22:20:05")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + serviceAuthentication)
                .when()
                .get()
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(200);

        return response
                .getBody()
                .asString();
    }
}
