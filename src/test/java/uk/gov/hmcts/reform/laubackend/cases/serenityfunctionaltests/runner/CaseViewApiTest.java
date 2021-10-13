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
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseViewRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseViewResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.CaseViewGetApiSteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps.CaseViewPostAPISteps;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants;

import java.text.ParseException;
import java.util.Map;

@RunWith(SerenityRunner.class)
public class CaseViewApiTest {

    @Steps
    CaseViewGetApiSteps caseViewGetApiSteps;
    @Steps
    CaseViewPostAPISteps caseViewPostAPISteps;

    @Test
    @Title("Assert response code of 200 for GET CaseViewApi with valid headers and valid request params")
    public void assertHttpSuccessResponseCodeForCaseViewApi() throws JsonProcessingException, ParseException {

        String authServiceToken = caseViewGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseViewGetApiSteps.givenValidParamsAreSuppliedForGetCaseView();
        Response response = caseViewGetApiSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        ObjectMapper objectMapper = new ObjectMapper();
        CaseViewResponseVO caseViewResponseVO = objectMapper.readValue(
            response.getBody().asString(),
            CaseViewResponseVO.class
        );

        caseViewGetApiSteps.thenASuccessResposeIsReturned(response);
        caseViewGetApiSteps.thenAtLeastOneRecordNumberShouldExist(response);
        caseViewGetApiSteps.thenTheGetCaseViewResponseParamsMatchesTheInput(queryParamMap, caseViewResponseVO);
        String successOrFailure = caseViewGetApiSteps.thenTheGetCaseViewResponseDateRangeMatchesTheInput(
            queryParamMap,
            caseViewResponseVO
        );
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS, "The assertion for GET CaseView API response code 200 is not successful");
    }

    @Test
    @Title("Assert response code of 401 for GET CaseViewApi service with Invalid Service Authentication Token")
    public void assertResponseCodeOf401WithInvalidServiceAuthenticationTokenForGetCaseViewApi() {

        String invalidServiceToken = caseViewGetApiSteps.givenTheInvalidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseViewGetApiSteps.givenValidParamsAreSuppliedForGetCaseView();
        Response response = caseViewGetApiSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            invalidServiceToken,
            queryParamMap
        );
        String successOrFailure = caseViewGetApiSteps.thenBadResponseForServiceAuthorizationIsReturned(response, 401);
        Assert.assertEquals( successOrFailure, TestConstants.SUCCESS, "CaseView API response code 401 assertion is not successful");
    }

    @Test
    @Title("Assert response code of 400 for GET CaseViewApi with Empty Params")
    public void assertResponseCodeOf400WithInvalidParamsForCaseViewApi() {
        String authServiceToken = caseViewGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseViewGetApiSteps.givenEmptyParamsAreSuppliedForGetCaseView();
        Response response = caseViewGetApiSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        String successOrFailure = caseViewGetApiSteps.thenBadResponseForServiceAuthorizationIsReturned(response, 400);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS, "The assertion is not successful");
    }


    @Test
    @Title("Assert response code of 200 for POST Request CaseViewApi")
    public void assertHttpSuccessResponseCodeForPostRequestCaseViewApi() throws com.fasterxml.jackson.core.JsonProcessingException {

        String authServiceToken = caseViewGetApiSteps.givenAValidServiceTokenIsGenerated();
        CaseViewRequestVO caseViewRequestVO = caseViewPostAPISteps.generateCaseViewPostRequestBody();
        Response response = caseViewPostAPISteps.whenThePostServiceIsInvoked(authServiceToken, caseViewRequestVO);
        String successOrFailure = caseViewGetApiSteps.thenASuccessResposeIsReturned(response);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS, "CaseView POST API response code 200 assertion is not successful");
    }

    @AfterClass
    public static void deleteAll() {
    }

}
