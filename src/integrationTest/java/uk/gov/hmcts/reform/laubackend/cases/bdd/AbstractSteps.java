package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.boot.web.server.LocalServerPort;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.laubackend.cases.helper.RestHelper;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static feign.form.ContentProcessor.CONTENT_TYPE_HEADER;
import static java.util.List.of;
import static uk.gov.hmcts.reform.laubackend.cases.bdd.WiremokInstantiator.INSTANCE;

@Getter
public class AbstractSteps {

    private static final String JSON_RESPONSE = "application/json;charset=UTF-8";
    public final WiremokInstantiator wiremokInstantiator = INSTANCE;
    protected final RestHelper restHelper = new RestHelper();
    protected final Gson jsonReader = new Gson();


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
                        .withBody("lau_frontend")));
        wiremokInstantiator.getWireMockServer().stubFor(get(urlPathMatching("/o/userinfo"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, JSON_RESPONSE)
                        .withStatus(200)
                        .withBody(getUserInfoAsJson())));
    }

    private String getUserInfoAsJson() {
        return jsonReader.toJson(new UserInfo("sub",
                "uid",
                "test",
                "given_name",
                "family_Name", of("cft-audit-investigator")));
    }
}
