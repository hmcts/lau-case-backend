package uk.gov.hmcts.reform.laubackend.cases.controllers;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@Tag("testType:Functional")
class EndpointSecurityTest extends LauCaseBaseFunctionalTest {


    @Test
    void shouldAllowUnauthenticatedRequestsToHealthCheck() {

        String response = lauCaseBackEndServiceClient.getHealthPage();

        assertThat(response).contains("UP");
    }
}
