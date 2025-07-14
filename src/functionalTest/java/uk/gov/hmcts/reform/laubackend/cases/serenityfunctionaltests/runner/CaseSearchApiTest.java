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
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseSearchGetResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseSearchRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.CaseSearchGetApiSteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.CaseSearchPostApiSteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.helper.DatabaseCleaner.deleteRecord;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_CASE_SEARCH_DELETE_ENDPOINT;

@RunWith(SerenityRunner.class)
@SuppressWarnings({"PMD.LawOfDemeter"})
public class CaseSearchApiTest {
    @Steps
    CaseSearchPostApiSteps caseSearchPostApiSteps;
    @Steps
    CaseSearchGetApiSteps caseSearchGetApiSteps;


    @Test
    @Title("Assert response code of 201 for POST Request CaseSearchApi")
    public void assertHttpSuccessResponseCodeForPostRequestCaseSearchApi()
            throws com.fasterxml.jackson.core.JsonProcessingException, JSONException {

        String authServiceToken = caseSearchGetApiSteps.givenAValidServiceTokenIsGenerated();

        CaseSearchRequestVO caseSearchRequestVO = caseSearchPostApiSteps.generateCaseSearchPostRequestBody();
        Response response = caseSearchPostApiSteps.whenThePostServiceIsInvoked(authServiceToken, caseSearchRequestVO);
        String successOrFailure = caseSearchGetApiSteps.thenASuccessResposeIsReturned(response);
        Assert.assertEquals(
                successOrFailure,
                TestConstants.SUCCESS,
                "CaseAction POST API response code 201 assertion is successful"
        );

        deleteRecord(AUDIT_CASE_SEARCH_DELETE_ENDPOINT,true, response);
    }

    @Test
    @Title("Assert response code of 400 for Invalid POST request body for CaseSearchApi")
    public void assertHttpBadResponseCodeForInvalidPostRequestBodyCaseSearchApi()
            throws com.fasterxml.jackson.core.JsonProcessingException {

        String authServiceToken = caseSearchGetApiSteps.givenAValidServiceTokenIsGenerated();
        CaseSearchRequestVO caseSearchRequestVO = caseSearchPostApiSteps.generateInvalidCaseSearchPostRequestBody();
        Response response = caseSearchPostApiSteps.whenThePostServiceIsInvoked(authServiceToken, caseSearchRequestVO);
        String successOrFailure = caseSearchGetApiSteps.thenBadResponseIsReturned(response, 400);
        Assert.assertEquals(
                successOrFailure,
                TestConstants.SUCCESS,
                "CaseAction POST API response code 400 assertion is not successful"
        );
    }

    @Test
    @Title("Assert response code of 200 for GET CaseSearchApi with valid headers and valid request params")
    public void assertHttpSuccessResponseCodeForCaseViewApi()
        throws IOException, ParseException, JSONException {

        String authServiceToken = caseSearchGetApiSteps.givenAValidServiceTokenIsGenerated();
        final String authorizationToken = caseSearchGetApiSteps.validAuthorizationTokenIsGenerated();
        Map<String, String> queryParamMap = caseSearchGetApiSteps.givenValidParamsAreSuppliedForGetCaseSearchApi();
        CaseSearchRequestVO caseSearchRequestVO = caseSearchPostApiSteps.generateCaseSearchPostRequestBody();
        final Response postResponse = caseSearchPostApiSteps
                .whenThePostServiceIsInvoked(authServiceToken, caseSearchRequestVO);
        Response response = caseSearchGetApiSteps.whenTheGetCaseSearchServiceIsInvokedWithTheGivenParams(
                authServiceToken,
                authorizationToken,
                queryParamMap
        );
        ObjectMapper objectMapper = new ObjectMapper();
        CaseSearchGetResponseVO caseSearchGetResponseVO = objectMapper.readValue(
                response.getBody().asString(),
                CaseSearchGetResponseVO.class
        );

        caseSearchGetApiSteps.thenASuccessResposeIsReturned(response);
        caseSearchGetApiSteps.thenAtLeastOneRecordNumberShouldExist(response);
        caseSearchGetApiSteps.thenTheGetCaseSearchResponseParamsMatchesTheInput(queryParamMap, caseSearchGetResponseVO);
        String successOrFailure = caseSearchGetApiSteps.thenTheGetCaseSearchResponseDateRangeMatchesTheInput(
                queryParamMap,
                caseSearchGetResponseVO
        );
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                "The assertion for GET CaseAction API response code 200 is not successful"
        );

        deleteRecord(AUDIT_CASE_SEARCH_DELETE_ENDPOINT,true, postResponse);
    }

    @Test
    @Title("Assert response code of 403 for GET CaseSearchApi service with Invalid ServiceAuthorization Token")
    public void assertResponseCodeOf403WithInvalidServiceAuthenticationTokenForGetCaseViewApi() throws JSONException {

        String invalidServiceToken = caseSearchGetApiSteps.givenTheInvalidServiceTokenIsGenerated();
        String authorizationToken = caseSearchGetApiSteps.validAuthorizationTokenIsGenerated();
        Map<String, String> queryParamMap = caseSearchGetApiSteps.givenValidParamsAreSuppliedForGetCaseSearchApi();
        Response response = caseSearchGetApiSteps.whenTheGetCaseSearchServiceIsInvokedWithTheGivenParams(
                invalidServiceToken,
                authorizationToken,
                queryParamMap
        );
        String successOrFailure = caseSearchGetApiSteps.thenBadResponseIsReturned(response, FORBIDDEN.value());
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                "CaseAction API response code 403 assertion is not successful"
        );
    }

    @Test
    @Title("Assert response code of 401 for GET CaseSearchApi service with Invalid ServiceAuthorization Token")
    public void assertResponseCodeOf401WithInvalidServiceAuthorizationTokenForGetCaseSearchApi() throws JSONException {

        String validServiceToken = caseSearchGetApiSteps.givenAValidServiceTokenIsGenerated();
        String ivalidAuthorizationToken = caseSearchGetApiSteps.givenTheInvalidAuthorizationTokenIsGenerated();
        Map<String, String> queryParamMap = caseSearchGetApiSteps.givenValidParamsAreSuppliedForGetCaseSearchApi();
        Response response = caseSearchGetApiSteps.whenTheGetCaseSearchServiceIsInvokedWithTheGivenParams(
                validServiceToken,
                ivalidAuthorizationToken,
                queryParamMap
        );
        String successOrFailure = caseSearchGetApiSteps.thenBadResponseIsReturned(response, UNAUTHORIZED.value());
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                "CaseAction API response code 403 assertion is not successful"
        );
    }

    @Test
    @Title("Assert response code of 400 for GET CaseSearchApi with Empty Params")
    public void assertResponseCodeOf400WithInvalidParamsForCaseViewApi() throws JSONException {
        String authServiceToken = caseSearchGetApiSteps.givenAValidServiceTokenIsGenerated();
        String authorizationToken = caseSearchGetApiSteps.validAuthorizationTokenIsGenerated();
        Map<String, String> queryParamMap = caseSearchGetApiSteps.givenEmptyParamsAreSuppliedForGetCaseSearchApi();
        Response response = caseSearchGetApiSteps.whenTheGetCaseSearchServiceIsInvokedWithTheGivenParams(
                authServiceToken,
                authorizationToken,
                queryParamMap
        );
        String successOrFailure = caseSearchGetApiSteps.thenBadResponseIsReturned(response, 400);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS, "The assertion is not successful");
    }
}
