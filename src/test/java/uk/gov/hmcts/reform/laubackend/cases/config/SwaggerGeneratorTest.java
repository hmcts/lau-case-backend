package uk.gov.hmcts.reform.laubackend.cases.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.laubackend.cases.authorization.AsyncAuthService;
import uk.gov.hmcts.reform.laubackend.cases.authorization.AuthService;
import uk.gov.hmcts.reform.laubackend.cases.authorization.AuthorisedServices;
import uk.gov.hmcts.reform.laubackend.cases.authorization.RestApiPreInvokeDeleteInterceptor;
import uk.gov.hmcts.reform.laubackend.cases.authorization.RestApiPreInvokeInterceptor;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Built-in feature which saves service's swagger specs in temporary directory.
 * Each travis run on master should automatically save and upload (if updated) documentation.
 */

@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnitTestShouldIncludeAssert", "PMD.LawOfDemeter"})
@SpringBootTest
@TestPropertySource(properties = {"idam.api.url = localhost:5000", "idam.s2s-auth.url = localhost:4502"})
class SwaggerGeneratorTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webAppContext;

    @MockitoBean
    private AsyncAuthService asyncAuthService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthorisedServices authorisedServices;

    @MockitoBean
    private RestApiPreInvokeInterceptor restApiPreInvokeInterceptor;

    @MockitoBean
    private RestApiPreInvokeDeleteInterceptor restApiPreInvokeDeleteInterceptor;

    @MockitoBean
    private IdamApi idamApi;

    @BeforeEach
    public void setUp() {
        this.mvc = webAppContextSetup(webAppContext).build();
    }

    @DisplayName("Generate swagger documentation")
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void generateDocs() throws Exception {
        byte[] specs = mvc.perform(get("/v2/api-docs"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsByteArray();

        try (OutputStream outputStream = Files.newOutputStream(Paths.get("/tmp/lau-case-backend.json"))) {
            outputStream.write(specs);
        }

    }
}
