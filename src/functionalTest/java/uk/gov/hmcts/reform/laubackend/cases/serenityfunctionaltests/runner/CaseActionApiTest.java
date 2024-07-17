package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.runner;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseActionRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseActionResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.CaseActionGetApiSteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.CaseActionPostApiSteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.helper.DatabaseCleaner.deleteRecord;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_CASE_ACTION_DELETE_ENDPOINT;

@RunWith(SerenityRunner.class)
public class CaseActionApiTest {

    @Steps
    CaseActionGetApiSteps caseActionGetApiSteps;
    @Steps
    CaseActionPostApiSteps caseActionPostApiSteps;


    @Test
    @Title("Assert response code of 200 for GET CaseActionApi with valid headers and valid request params")
    public void assertHttpSuccessResponseCodeForCaseViewApi() throws Exception {

        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        final String authorizationToken = caseActionGetApiSteps.validAuthorizationTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenValidParamsAreSuppliedForGetCaseAction();
        CaseActionRequestVO caseActionRequestVO = caseActionPostApiSteps.generateCaseActionPostRequestBody();
        final Response postResponse = caseActionPostApiSteps
                .whenThePostServiceIsInvoked(authServiceToken, caseActionRequestVO);
        Response response = caseActionGetApiSteps.whenTheGetCaseActionServiceIsInvokedWithTheGivenParams(
                authServiceToken,
                authorizationToken,
                queryParamMap
        );
        ObjectMapper objectMapper = new ObjectMapper();
        CaseActionResponseVO caseActionResponseVO = objectMapper.readValue(
                response.getBody().asString(),
                CaseActionResponseVO.class
        );

        caseActionGetApiSteps.thenASuccessResposeIsReturned(response);
        caseActionGetApiSteps.thenAtLeastOneRecordNumberShouldExist(response);
        caseActionGetApiSteps.thenTheGetCaseActionResponseParamsMatchesTheInput(queryParamMap, caseActionResponseVO);
        String successOrFailure = caseActionGetApiSteps.thenTheGetCaseViewResponseDateRangeMatchesTheInput(
                queryParamMap,
                caseActionResponseVO
        );
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                "The assertion for GET CaseAction API response code 200 is not successful"
        );

        deleteRecord(AUDIT_CASE_ACTION_DELETE_ENDPOINT, false, postResponse);
    }

    @Test
    @Title("Assert response code od f 200 for GET jurisdictionsCaseTypes with valid headers")
    public void assertHttpSuccessResponseCodeForJurisdictionsCaseTypesApi() throws JSONException {
        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        final String authorizationToken = caseActionGetApiSteps.validAuthorizationTokenIsGenerated();
        Response response = caseActionGetApiSteps.whenTheGetJurisdictionsCaseTypesServiceIsInvoked(
                authServiceToken,
                authorizationToken
        );
        String successOrFailure = caseActionGetApiSteps.thenASuccessResposeIsReturned(response);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                "The assertion for GET jurisdictionsCaseTypes API response code 200 is not successful"
        );
        assertThat(response.getBody().asString())
            .contains("{\"jurisdictions\":[")
            .contains("\"caseTypes\":[");
    }

    @Test
    @Title("Assert response code of 403 for GET CaseActionApi service with Invalid ServiceAuthorization Token")
    public void assertResponseCodeOf403WithInvalidServiceAuthenticationTokenForGetCaseViewApi() throws JSONException {

        String invalidServiceToken = caseActionGetApiSteps.givenTheInvalidServiceTokenIsGenerated();
        final String authorizationToken = caseActionGetApiSteps.validAuthorizationTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenValidParamsAreSuppliedForGetCaseAction();
        Response response = caseActionGetApiSteps.whenTheGetCaseActionServiceIsInvokedWithTheGivenParams(
                invalidServiceToken,
                authorizationToken,
                queryParamMap
        );
        String successOrFailure = caseActionGetApiSteps.thenBadResponseIsReturned(response, FORBIDDEN.value());
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                "CaseAction API response code 403 assertion is not successful"
        );
    }

    @Test
    @Title("Assert response code of 401 for GET CaseActionApi service with Invalid Authorization Token")
    public void assertResponseCodeOf401WithInvalidAuthenticationTokenForGetCaseViewApi() throws JSONException {

        final String validServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        final String invalidAuthorizationToken = caseActionGetApiSteps.givenTheInvalidAuthorizationTokenIsGenerated();
        final Map<String, String> queryParamMap = caseActionGetApiSteps.givenValidParamsAreSuppliedForGetCaseAction();
        final Response response = caseActionGetApiSteps.whenTheGetCaseActionServiceIsInvokedWithTheGivenParams(
                validServiceToken,
                invalidAuthorizationToken,
                queryParamMap
        );
        String successOrFailure = caseActionGetApiSteps.thenBadResponseIsReturned(response, UNAUTHORIZED.value());
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                "CaseAction API response code 401 assertion is not successful"
        );
    }

    @Test
    @Title("Assert response code of 400 for GET CaseActionApi with Empty Params")
    public void assertResponseCodeOf400WithInvalidParamsForCaseViewApi() throws JSONException {
        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        final String authorizationToken = caseActionGetApiSteps.validAuthorizationTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenEmptyParamsAreSuppliedForGetCaseAction();
        Response response = caseActionGetApiSteps.whenTheGetCaseActionServiceIsInvokedWithTheGivenParams(
                authServiceToken,
                authorizationToken,
                queryParamMap
        );
        String successOrFailure = caseActionGetApiSteps.thenBadResponseIsReturned(response, 400);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS, "The assertion is not successful");
    }


    @Test
    @Title("Assert response code of 201 for POST Request CaseActionApi")
    public void assertHttpSuccessResponseCodeForPostRequestCaseViewApi()
            throws com.fasterxml.jackson.core.JsonProcessingException, JSONException {

        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        CaseActionRequestVO caseActionRequestVO = caseActionPostApiSteps.generateCaseActionPostRequestBody();
        Response response = caseActionPostApiSteps.whenThePostServiceIsInvoked(authServiceToken, caseActionRequestVO);
        String successOrFailure = caseActionGetApiSteps.thenASuccessResposeIsReturned(response);
        Assert.assertEquals(
                successOrFailure,
                TestConstants.SUCCESS,
                "CaseAction POST API response code 200 assertion is not successful"
        );

        deleteRecord(AUDIT_CASE_ACTION_DELETE_ENDPOINT, false, response);
    }

}
