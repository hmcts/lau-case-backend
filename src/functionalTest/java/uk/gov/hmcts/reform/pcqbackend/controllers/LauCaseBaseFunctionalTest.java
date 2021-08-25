package uk.gov.hmcts.reform.pcqbackend.controllers;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.cases.client.LauCaseBackEndServiceClient;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ComponentScan("uk.gov.hmcts.reform.pcqbackend")
@TestPropertySource("classpath:application-functional.yaml")
@Slf4j
@SuppressWarnings({"PMD.AbstractClassWithoutAnyMethod", "PMD.AbstractClassWithoutAbstractMethod"})
public abstract class LauCaseBaseFunctionalTest {

    @Value("${targetInstance}")
    protected String lauCaseBackEndApiUrl;

    protected LauCaseBackEndServiceClient lauCaseBackEndServiceClient;

    @Before
    public void setUp() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.defaultParser = Parser.JSON;

        lauCaseBackEndServiceClient = new LauCaseBackEndServiceClient(lauCaseBackEndApiUrl);
    }
}
