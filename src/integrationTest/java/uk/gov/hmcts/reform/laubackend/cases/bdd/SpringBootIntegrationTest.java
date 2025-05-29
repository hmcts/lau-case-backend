package uk.gov.hmcts.reform.laubackend.cases.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.laubackend.cases.Application;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@CucumberContextConfiguration
//@ContextConfiguration(classes = {AsyncConfiguration.class})
//@Import(AsyncConfiguration.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@Slf4j
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class SpringBootIntegrationTest {
}
