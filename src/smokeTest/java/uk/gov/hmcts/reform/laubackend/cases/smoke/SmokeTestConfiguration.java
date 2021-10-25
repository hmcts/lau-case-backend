package uk.gov.hmcts.reform.laubackend.cases.smoke;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("uk.gov.hmcts.reform.laubackend.cases.smoke")
@PropertySource("application.properties")
public class SmokeTestConfiguration {
}
