package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static feign.form.ContentProcessor.CONTENT_TYPE_HEADER;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Getter
public class AbstractSteps {

    public static final String SERVICE_AUTHORISATION_HEADER = "ServiceAuthorization";
    private static final String JSON_RESPONSE = "application/json;charset=UTF-8";

    private final WireMockServer wireMockServer = new WireMockServer(4554);

    @Value("${idam.s2s-auth.name-cs}")
    private String s2sName;

    @Value("${idam.s2s-auth.secret-cs}")
    private String s2sSecret;

    @Value("${idam.s2s-auth.url}")
    private String s2sUrl;

    @LocalServerPort
    private int port;

    protected String baseUrl() {
        return "http://localhost:" + port;
    }


    public void setupS2SAuthorisationStub() {
        wireMockServer.start();
        wireMockServer.stubFor(post(urlPathMatching("/lease"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody("s2sExampleHeader")));
    }

    public void setupServiceAuthorisationStub() {
        wireMockServer.start();
        wireMockServer.stubFor(get(urlPathMatching("/details"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, JSON_RESPONSE)
                        .withStatus(200)
                        .withBody("lau_case_frontend")));
    }
}
