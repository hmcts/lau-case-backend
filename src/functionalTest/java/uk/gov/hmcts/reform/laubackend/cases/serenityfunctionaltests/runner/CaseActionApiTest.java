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
    @Title("Assert response code of 200 for GET CaseActionApi with valid headers and valid request params")
    public void assertHttpSuccessResponseCodeForCaseViewApi() throws JsonProcessingException, ParseException {

        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenValidParamsAreSuppliedForGetCaseAction();
        Response response = caseActionGetApiSteps.whenTheGetCaseActionServiceIsInvokedWithTheGivenParams(
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
        caseActionGetApiSteps.thenTheGetCaseActionResponseParamsMatchesTheInput(queryParamMap, caseActionResponseVO);
        String successOrFailure = caseActionGetApiSteps.thenTheGetCaseViewResponseDateRangeMatchesTheInput(
            queryParamMap,
            caseActionResponseVO
        );
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                            "The assertion for GET CaseAction API response code 200 is not successful"
        );
    }

    @Test
    @Title("Assert response code of 403 for GET CaseActionApi service with Invalid ServiceAuthorization Token")
    public void assertResponseCodeOf401WithInvalidServiceAuthenticationTokenForGetCaseViewApi() {

        String invalidServiceToken = caseActionGetApiSteps.givenTheInvalidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenValidParamsAreSuppliedForGetCaseAction();
        Response response = caseActionGetApiSteps.whenTheGetCaseActionServiceIsInvokedWithTheGivenParams(
            invalidServiceToken,
            queryParamMap
        );
        String successOrFailure = caseActionGetApiSteps.thenBadResponseIsReturned(response, 401);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS,
                            "CaseAction API response code 401 assertion is not successful"
        );
    }

    @Test
    @Title("Assert response code of 400 for GET CaseActionApi with Empty Params")
    public void assertResponseCodeOf400WithInvalidParamsForCaseViewApi() {
        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = caseActionGetApiSteps.givenEmptyParamsAreSuppliedForGetCaseAction();
        Response response = caseActionGetApiSteps.whenTheGetCaseActionServiceIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        String successOrFailure = caseActionGetApiSteps.thenBadResponseIsReturned(response, 400);
        Assert.assertEquals(successOrFailure, TestConstants.SUCCESS, "The assertion is not successful");
    }


    @Test
    @Title("Assert response code of 201 for POST Request CaseActionApi")
    public void assertHttpSuccessResponseCodeForPostRequestCaseViewApi()
        throws com.fasterxml.jackson.core.JsonProcessingException {

        String authServiceToken = caseActionGetApiSteps.givenAValidServiceTokenIsGenerated();
        CaseActionRequestVO caseActionRequestVO = caseActionPostApiSteps.generateCaseActionPostRequestBody();
        Response response = caseActionPostApiSteps.whenThePostServiceIsInvoked(authServiceToken, caseActionRequestVO);
        String successOrFailure = caseActionGetApiSteps.thenASuccessResposeIsReturned(response);
        Assert.assertEquals(
            successOrFailure,
            TestConstants.SUCCESS,
            "CaseAction POST API response code 200 assertion is not successful"
        );
    }

    @AfterClass
    public static void deleteAll() {
    }

}
