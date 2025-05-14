package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.laubackend.cases.helper.RestHelper;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static feign.form.ContentProcessor.CONTENT_TYPE_HEADER;
import static java.util.List.of;
import static uk.gov.hmcts.reform.laubackend.cases.bdd.WiremokInstantiator.INSTANCE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_AUDIT_INVESTIGATOR_ROLE;

@Getter
@SuppressWarnings("PMD.LawOfDemeter")
public class AbstractSteps {

    private static final String JSON_RESPONSE = "application/json;charset=UTF-8";
    protected final RestHelper restHelper = new RestHelper();
    protected final Gson jsonReader = new Gson();


    @LocalServerPort
    private int port;

    public String baseUrl() {
        return "http://localhost:" + port;
    }

    public void setupAuthorisationStub() {
        setupAuthorisationStubWithRole(AUTHORISATION_AUDIT_INVESTIGATOR_ROLE);
    }

    public void setupAuthorisationStubWithRole(String role) {
        WireMockServer server = INSTANCE.getWireMockServer();
        server.stubFor(get(urlPathMatching("/details"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, JSON_RESPONSE)
                        .withStatus(200)
                        .withBody("lau_frontend")));
        server.stubFor(get(urlPathMatching("/o/userinfo"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, JSON_RESPONSE)
                        .withStatus(403)
                        .withBody(getUserInfoAsJson(role))));
    }

    private String getUserInfoAsJson(String role) {
        return jsonReader.toJson(new UserInfo("sub",
                "uid",
                "test",
                "given_name",
                "family_Name", of(role)));
    }
}
