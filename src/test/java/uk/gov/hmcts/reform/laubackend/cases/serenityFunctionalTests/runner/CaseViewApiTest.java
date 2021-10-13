package uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.runner;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.model.CaseViewRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.model.CaseViewResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.steps.CaseViewSteps;

import java.util.HashMap;

@RunWith(SerenityRunner.class)
public class CaseViewApiTest {

    @Steps
    CaseViewSteps caseViewSteps;

    @Test
    @Title("Assert response code of 200 for CaseViewApi")
    public void assertHttpSuccessResponseCodeForCaseViewApi() {

        String authServiceToken = caseViewSteps.givenAValidServiceTokenIsGenerated();
        HashMap<String, String> queryParamMap = caseViewSteps.givenValidParamsAreSuppliedForGetCaseView();
        Response response = caseViewSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        CaseViewResponseVO caseViewResponseVO = null;
        try {
            System.out.println("case view response : " + response.getBody().asString());
            ObjectMapper objectMapper = new ObjectMapper();
            caseViewResponseVO = objectMapper.readValue(
                response.getBody().asString(),
                CaseViewResponseVO.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        caseViewSteps.thenASuccessResposeIsReturned(response);
        caseViewSteps.thenAtLeastOneRecordNumberShouldExist(response);
        caseViewSteps.thenTheGetCaseViewResponseParamsMatchesTheInput(queryParamMap, caseViewResponseVO);
        caseViewSteps.thenTheGetCaseViewResponseDateRangeMatchesTheInput(queryParamMap, caseViewResponseVO);
    }

    @Test
    @Title("Assert response code of 401 with Invalid Service Authentication Token for Get CaseViewApi service")
    public void AssertResponseCodeOf401WithInvalidServiceAuthenticationTokenForGetCaseViewApi() {

        String invalidServiceToken = caseViewSteps.givenTheInvalidServiceTokenIsGenerated();
        HashMap<String, String> queryParamMap = caseViewSteps.givenValidParamsAreSuppliedForGetCaseView();
        Response response = caseViewSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            invalidServiceToken,
            queryParamMap
        );
        caseViewSteps.thenBadResponseForServiceAuthorizationIsReturned(response, 401);
    }

    @Test
    @Title("Assert response code of 400 with Empty Params for CaseViewApi")
    public void AssertResponseCodeOf400WithInvalidParamsForCaseViewApi() {
        String authServiceToken = caseViewSteps.givenAValidServiceTokenIsGenerated();
        HashMap<String, String> queryParamMap = caseViewSteps.givenEmptyParamsAreSuppliedForGetCaseView();
        Response response = caseViewSteps.whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        caseViewSteps.thenBadResponseForServiceAuthorizationIsReturned(response, 400);
    }


    @Test
    @Title("Assert response code of 200 for POST Request CaseViewApi")
    public void assertHttpSuccessResponseCodeForPostRequestCaseViewApi() {

        String authServiceToken = caseViewSteps.givenAValidServiceTokenIsGenerated();
        CaseViewRequestVO caseViewRequestVO = caseViewSteps.generateCaseViewPostRequestBody();
        Response response = caseViewSteps.whenThePostServiceIsInvoked(authServiceToken, caseViewRequestVO);
        caseViewSteps.thenASuccessResposeIsReturned(response);

    }

    @AfterClass
    public static void deleteAll() {
//        new BaseSteps().deleteAllSync();
    }

}
