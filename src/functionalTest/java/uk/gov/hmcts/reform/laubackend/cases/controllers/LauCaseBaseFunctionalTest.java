package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import uk.gov.hmcts.reform.laubackend.cases.client.LauCaseBackEndServiceClient;

@Slf4j
@SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod"})
public abstract class LauCaseBaseFunctionalTest {

    private static final String DEFAULT_TARGET_INSTANCE = "http://localhost:4550";

    protected String lauCaseBackEndApiUrl;

    protected LauCaseBackEndServiceClient lauCaseBackEndServiceClient;

    @BeforeEach
    public void setUp() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.defaultParser = Parser.JSON;

        lauCaseBackEndApiUrl = System.getenv().getOrDefault("TEST_URL", DEFAULT_TARGET_INSTANCE);
        lauCaseBackEndServiceClient = new LauCaseBackEndServiceClient(lauCaseBackEndApiUrl);
    }
}
