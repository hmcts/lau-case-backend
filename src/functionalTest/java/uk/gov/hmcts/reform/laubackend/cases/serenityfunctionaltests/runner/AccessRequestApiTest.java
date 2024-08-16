package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.runner;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.AccessRequestPostApiSteps;

import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.helper.DatabaseCleaner.deleteAccessRequestRecord;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_ACCESS_REQUEST_DELETE_ENDPOINT;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_ACCESS_REQUEST_ENDPOINT;

@RunWith(SerenityRunner.class)
public class AccessRequestApiTest {

    @Steps
    AccessRequestPostApiSteps postSteps;

    @Test
    @Title("Assert that response code of 201 is returned for POST /audit/accessRequest")
    public void assertHttpSuccessResponseCodeForPostAccessRequestApi() {
        String serviceToken = postSteps.givenAValidServiceTokenIsGenerated();
        String requestBody = postSteps.generateAccessRequestPostRequestBody();
        Response response = postSteps.performPostOperation(AUDIT_ACCESS_REQUEST_ENDPOINT, requestBody, serviceToken);
        postSteps.assertSuccessResponse(response, HttpStatus.SC_CREATED);

        deleteAccessRequestRecord(AUDIT_ACCESS_REQUEST_DELETE_ENDPOINT, response);
    }

}
