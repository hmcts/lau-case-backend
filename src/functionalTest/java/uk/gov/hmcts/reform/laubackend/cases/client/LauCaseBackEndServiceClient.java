package uk.gov.hmcts.reform.laubackend.cases.client;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.http.HttpStatus;

@Slf4j
@SuppressWarnings({"unchecked", "PMD.AvoidDuplicateLiterals"})
public class LauCaseBackEndServiceClient {

    private final String lauCaseBackEndApiUrl;

    public LauCaseBackEndServiceClient(String lauCaseBackEndApiUrl) {
        this.lauCaseBackEndApiUrl = lauCaseBackEndApiUrl;
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    public String getWelcomePage() {
        return SerenityRest
            .get(lauCaseBackEndApiUrl)
            .then()
            .statusCode(HttpStatus.OK.value())
            .and()
            .extract()
            .body()
            .asString();
    }

    @SuppressWarnings("PMD.LawOfDemeter")
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
