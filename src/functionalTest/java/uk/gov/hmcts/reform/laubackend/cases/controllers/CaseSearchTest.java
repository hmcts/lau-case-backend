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
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import static java.util.Arrays.asList;
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
public class CaseSearchTest {

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
    public void shouldPostCaseSearchWithS2SToken() {
        try {
            final String s2sString = idamServiceClient.s2sSignIn();
            assertNotNull("S2S String should contain a value", s2sString);
            final String caseSearch = postCaseSearch(s2sString);

            assertThat(caseSearch).contains("searchLog");

        } catch (final Exception e) {
            fail("Unable to successfully retrieve SAS token: " + e.getMessage());
        }
    }


    public String postCaseSearch(final String serviceAuthentication) {
        final Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(lauCaseBackEndApiUrl + "/audit/caseSearch")
                .body(getCaseSearchPostRequest())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + serviceAuthentication)
                .when()
                .post()
                .andReturn();

        assertThat(response.getStatusCode()).isEqualTo(201);

        return response
                .getBody()
                .asString();
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
