package uk.gov.hmcts.reform.laubackend.cases.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",
    plugin = {"pretty", "html:target/cucumber/cucumber-report.html"},
    monochrome = true)
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class CucumberTest {

}
