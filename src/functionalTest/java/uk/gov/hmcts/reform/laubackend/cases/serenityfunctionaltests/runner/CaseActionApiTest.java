package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.runner;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseActionRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseActionResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.CaseActionGetApiSteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.CaseActionPostApiSteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants;

import java.text.ParseException;
import java.util.Map;

@RunWith(SerenityRunner.class)
public class CaseActionApiTest {

    @Steps
    CaseActionGetApiSteps caseActionGetApiSteps;
    @Steps
    CaseActionPostApiSteps caseActionPostApiSteps;

    @Test
    @Title("Assert response code of 200 for GET CaseViewApi with valid headers and valid request params")
    public void assertHttpSuccessResponseCodeForCaseViewApi() throws JsonProcessingException, ParseException {

        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenValidParamsAreSuppliedForGetCaseView();
        Response response = caseActionGetApiSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        ObjectMapper objectMapper = new ObjectMapper();
        CaseActionResponseVO caseActionResponseVO = objectMapper.readValue(
            response.getBody().asString(),
            CaseActionResponseVO.class
        );

        caseActionGetApiSteps.thenASuccessResposeIsReturned(response);
        caseActionGetApiSteps.thenAtLeastOneRecordNumberShouldExist(response);
        caseActionGetApiSteps.thenTheGetCaseViewResponseParamsMatchesTheInput(queryParamMap, caseActionResponseVO);
        String successOrFailure = caseActionGetApiSteps.thenTheGetCaseViewResponseDateRangeMatchesTheInput(
            queryParamMap,
            caseActionResponseVO
        );
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                            "The assertion for GET CaseView API response code 200 is not successful"
        );
    }

    @Test
    @Title("Assert response code of 401 for GET CaseViewApi service with Invalid Service Authentication Token")
    public void assertResponseCodeOf401WithInvalidServiceAuthenticationTokenForGetCaseViewApi() {

        String invalidServiceToken = caseActionGetApiSteps.givenTheInvalidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenValidParamsAreSuppliedForGetCaseView();
        Response response = caseActionGetApiSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            invalidServiceToken,
            queryParamMap
        );
        String successOrFailure = caseActionGetApiSteps.thenBadResponseForServiceAuthorizationIsReturned(response, 401);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                            "CaseView API response code 401 assertion is not successful"
        );
    }

    @Test
    @Title("Assert response code of 400 for GET CaseViewApi with Empty Params")
    public void assertResponseCodeOf400WithInvalidParamsForCaseViewApi() {
        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenEmptyParamsAreSuppliedForGetCaseView();
        Response response = caseActionGetApiSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        String successOrFailure = caseActionGetApiSteps.thenBadResponseForServiceAuthorizationIsReturned(response, 400);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS, "The assertion is not successful");
    }


    @Test
    @Title("Assert response code of 200 for POST Request CaseViewApi")
    public void assertHttpSuccessResponseCodeForPostRequestCaseViewApi()
        throws com.fasterxml.jackson.core.JsonProcessingException {

        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        CaseActionRequestVO caseActionRequestVO = caseActionPostApiSteps.generateCaseViewPostRequestBody();
        Response response = caseActionPostApiSteps.whenThePostServiceIsInvoked(authServiceToken, caseActionRequestVO);
        String successOrFailure = caseActionGetApiSteps.thenASuccessResposeIsReturned(response);
        Assert.assertEquals(
            successOrFailure,
            TestConstants.SUCCESS,
            "CaseView POST API response code 200 assertion is not successful"
        );
    }

    @AfterClass
    public static void deleteAll() {
    }

}
