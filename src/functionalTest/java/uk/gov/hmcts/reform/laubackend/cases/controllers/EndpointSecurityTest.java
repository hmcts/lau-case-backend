package uk.gov.hmcts.reform.laubackend.cases.controllers;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
public class EndpointSecurityTest extends LauCaseBaseFunctionalTest {

    @Ignore
    public void shouldAllowUnauthenticatedRequestsToWelcomeMessage() {

        String response = lauCaseBackEndServiceClient.getWelcomePage();

        assertThat(response).contains("Welcome");
    }

    @Test
    public void shouldAllowUnauthenticatedRequestsToHealthCheck() {

        String response = lauCaseBackEndServiceClient.getHealthPage();

        assertThat(response).contains("UP");
    }
}
