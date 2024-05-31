package uk.gov.hmcts.reform.laubackend.cases.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI lauCaseBackendOpenApi() {
        License licence = new License().name("REFORM Common Components").url("");

        Info info = new Info().title("LAU Case Back-End API")
            .description("Log and Audit Case Back-End API, used for auditing case searches and actions.")
            .version("0.1")
            .license(licence);

        return new OpenAPI().info(info);
    }
}
