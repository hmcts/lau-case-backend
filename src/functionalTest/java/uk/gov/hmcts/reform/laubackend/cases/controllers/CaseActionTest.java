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
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;

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
public class CaseActionTest {

    public static final String SERVICE_AUTHORISATION_HEADER = "ServiceAuthorization";

    @Value("${targetInstance}")
    private String lauCaseBackEndApiUrl;

    @Value("${idam.s2s-auth.name-cs}")
    private String s2sName;

    @Value("${idam.s2s-auth.secret-cs}")
    private String s2sSecret;

    @Value("${idam.s2s-auth.url}")
    private String s2sUrl;

    private String s2sToken;

    @Before
    public void setUp() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.defaultParser = Parser.JSON;
        s2sToken = new IdamServiceClient(s2sName, s2sSecret, s2sUrl).s2sSignIn();
    }

    @Test
    @Ignore("This test has to be extended. We will need to add more assertions "
            + "and cover some other cases when functional test ticket is started")
    public void shouldGetCaseActionWithS2SToken() {
        try {
            assertNotNull("S2S String should contain a value", s2sToken);
            final String caseView = fetchCaseActionResponse();

            assertThat(caseView).contains("viewLog");

        } catch (final Exception e) {
            fail("Unable to successfully retrieve SAS token: " + e.getMessage());
        }
    }

    @Test
    @Ignore("This test has to be extended. We will need to add more assertions "
            + "and cover some other cases when functional test ticket is started")
    public void shouldPostCaseActionWithS2SToken() {
        try {
            assertNotNull("S2S String should contain a value", s2sToken);
            final String caseAction = postCaseAction();

            assertThat(caseAction).contains("caseAction");

        } catch (final Exception e) {
            fail("Unable to successfully retrieve SAS token: " + e.getMessage());
        }
    }

    private String postCaseAction() {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(lauCaseBackEndApiUrl + "/audit/caseAction")
                .body(getCaseActionPostRequest())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + s2sToken)
                .when()
                .post()
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(201);

        return response
                .getBody()
                .asString();
    }

    private CaseActionPostRequest getCaseActionPostRequest() {
        final ActionLog actionLog = new ActionLog("1",
                "R",
                "1615817621013640",
                "3",
                "4",
                "2021-08-23T22:20:05.023Z");

        final CaseActionPostRequest caseActionPostRequest = new CaseActionPostRequest();
        caseActionPostRequest.setActionLog(actionLog);

        return caseActionPostRequest;
    }


    public String fetchCaseActionResponse() {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(lauCaseBackEndApiUrl + "/audit/caseAction")
                .queryParam("startTimestamp", "2000-08-23T22:20:05")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + s2sToken)
                .when()
                .get()
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(200);

        return response
                .getBody()
                .asString();
    }
}
