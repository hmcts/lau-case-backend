package uk.gov.hmcts.reform.laubackend.cases.client;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.http.HttpStatus;

@Slf4j
public class LauCaseBackEndServiceClient {

    private final String lauCaseBackEndApiUrl;

    public LauCaseBackEndServiceClient(String lauCaseBackEndApiUrl) {
        this.lauCaseBackEndApiUrl = lauCaseBackEndApiUrl;
    }

    public String getHealthPage() {
        return SerenityRest
            .get(lauCaseBackEndApiUrl + "/health")
            .then()
            .statusCode(HttpStatus.OK.value())
            .and()
            .extract()
            .body()
            .asString();
    }
}
