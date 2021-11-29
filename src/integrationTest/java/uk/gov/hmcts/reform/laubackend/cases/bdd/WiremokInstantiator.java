package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;

import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
public enum WiremokInstantiator {
    INSTANCE;

    private final WireMockServer wireMockServer = new WireMockServer(4554);

    WiremokInstantiator() {
        try {
            wireMockServer.start();
            SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //DO NOTHING
        }
    }
}
