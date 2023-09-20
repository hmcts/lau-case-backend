package uk.gov.hmcts.reform.laubackend.cases.controllers;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
public class EndpointSecurityTest extends LauCaseBaseFunctionalTest {


    @Test
    public void shouldAllowUnauthenticatedRequestsToHealthCheck() {

        String response = lauCaseBackEndServiceClient.getHealthPage();

        assertThat(response).contains("UP");
    }
}
