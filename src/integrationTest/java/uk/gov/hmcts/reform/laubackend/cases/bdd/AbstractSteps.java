package uk.gov.hmcts.reform.laubackend.cases.bdd;

import lombok.Getter;
import org.springframework.boot.web.server.LocalServerPort;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static feign.form.ContentProcessor.CONTENT_TYPE_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.bdd.WiremokInstantiator.INSTANCE;

@Getter
public class AbstractSteps {

    public static final String SERVICE_AUTHORISATION_HEADER = "ServiceAuthorization";
    private static final String JSON_RESPONSE = "application/json;charset=UTF-8";
    public static final String AUTH_TOKEN = "some_exciting_super_cool_chars";
    public final WiremokInstantiator wiremokInstantiator = INSTANCE;

    @LocalServerPort
    private int port;

    public String baseUrl() {
        return "http://localhost:" + port;
    }

    public void setupServiceAuthorisationStub() {
        wiremokInstantiator.getWireMockServer().stubFor(get(urlPathMatching("/details"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, JSON_RESPONSE)
                        .withStatus(200)
                        .withBody("lau_case_frontend")));
    }
}
