package uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.steps;

import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.model.CaseViewRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.model.CaseViewResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.model.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.serenityFunctionalTests.utils.TestConstants;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaseViewSteps extends BaseSteps {

    private Logger logger = LoggerFactory.getLogger(CaseViewSteps.class);


    @Step("Given a valid service token is generated")
    public String givenAValidServiceTokenIsGenerated() {
        return getServiceToken(TestConstants.S2S_NAME);
    }

    @Step("When valid params are supplied for Get CaseView API")
    public HashMap<String, String> givenValidParamsAreSuppliedForGetCaseView() {
        HashMap<String, String> queryParamMap = new HashMap<String, String>();
        queryParamMap.put("userId", "3748240");
        queryParamMap.put("caseRef", "1615817621013549");
        queryParamMap.put("caseJurisdictionId", "CMC");
        queryParamMap.put("caseTypeId", "Caveats");
        queryParamMap.put("starttimestamp", "2021-08-22T22:19:05");
        queryParamMap.put("endtimestamp", "2021-08-23T22:20:06");
        return queryParamMap;
    }

    @Step("When the CaseView GET service is invoked with the valid params")
    public Response whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(String serviceToken, HashMap<String, String> queryParamMap) {
        return performGetOperation(TestConstants.AUDIT_CASE_VIEW_ENDPOINT, null, queryParamMap, serviceToken);
    }

    @Step("Then at least one record number should exist")
    public void thenAtLeastOneRecordNumberShouldExist(Response response) {
        response.then().assertThat().body("startRecordNumber", Matchers.is(Matchers.greaterThanOrEqualTo(1)));
    }

    @Step("Then the GET CaseView response params match the input")
    public void thenTheGetCaseViewResponseParamsMatchesTheInput(Map<String, String> inputQueryParamMap, CaseViewResponseVO caseViewResponseVO) {
        int startRecordNumber = caseViewResponseVO.getStartRecordNumber();
        Assert.assertTrue(startRecordNumber > 0);
        List<ViewLog> viewLogList = caseViewResponseVO.getViewLog();
        ViewLog viewLog = viewLogList.get(0);
        for (String queryParam : inputQueryParamMap.keySet()) {

            if (queryParam.equals("userId")) {
                Assert.assertEquals(
                    "User Id is missing in the response",
                    inputQueryParamMap.get(queryParam),
                    viewLog.getUserId()
                );
            } else if (queryParam.equals("caseRef")) {
                Assert.assertEquals(
                    "caseRef is missing in the response",
                    inputQueryParamMap.get(queryParam),
                    viewLog.getCaseRef()
                );

            } else if (queryParam.equals("caseJurisdictionId")) {
                Assert.assertEquals(
                    "caseJurisdictionId is missing in the response",
                    inputQueryParamMap.get(queryParam),
                    viewLog.getCaseJurisdictionId()
                );

            } else if (queryParam.equals("caseTypeId")) {
                Assert.assertEquals(
                    "caseTypeId is missing in the response",
                    inputQueryParamMap.get(queryParam),
                    viewLog.getCaseTypeId()
                );

            }
        }
    }

    @Step("Then the GET CaseView response date range matches the input")
    public void thenTheGetCaseViewResponseDateRangeMatchesTheInput(Map<String, String> inputQueryParamMap, CaseViewResponseVO caseViewResponseVO) {
        try {
            List<ViewLog> viewLogList = caseViewResponseVO.getViewLog();
            ViewLog viewLog = viewLogList.get(0);
            String timeStampResponse = viewLog.getTimestamp();
            String timeStampStartInputParam = inputQueryParamMap.get("starttimestamp");
            String timeStampEndInputParam = inputQueryParamMap.get("endtimestamp");

            String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
            String responseDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            Date inputStartTimestamp = new SimpleDateFormat(dateFormat).parse(timeStampStartInputParam);
            Date inputEndTimestamp = new SimpleDateFormat(dateFormat).parse(timeStampEndInputParam);
            Date responseTimestamp = new SimpleDateFormat(responseDateFormat).parse(timeStampResponse);

            logger.info("Input start date : " + inputStartTimestamp.getTime());
            logger.info("Input end date : " + inputEndTimestamp.getTime());
            logger.info("Output date : " + responseTimestamp.getTime());

            Assert.assertTrue(responseTimestamp.getTime() == inputStartTimestamp.getTime()
                                  || responseTimestamp.getTime() == inputEndTimestamp.getTime()
                                  || (responseTimestamp.after(inputStartTimestamp) && responseTimestamp.before(
                inputEndTimestamp))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Step("Given empty params values are supplied for the GET CaseView API")
    public HashMap<String, String> givenEmptyParamsAreSuppliedForGetCaseView() {
        HashMap<String, String> queryParamMap = new HashMap<String, String>();
        queryParamMap.put("userId", "");
        queryParamMap.put("caseRef", "");
        queryParamMap.put("caseJurisdictionId", "");
        queryParamMap.put("caseTypeId", "");
        queryParamMap.put("starttimestamp", "");
        queryParamMap.put("endtimestamp", "");
        return queryParamMap;
    }

    @Step("Given the invalid service authorization token is generated")
    public String givenTheInvalidServiceTokenIsGenerated() {
        String authServiceToken = givenAValidServiceTokenIsGenerated();
        String invalidServiceToken = authServiceToken + "abc";
        return invalidServiceToken;
    }

    @Step("Then 401 response is returned")
    public void thenBadResponseForServiceAuthorizationIsReturned(Response response, int expectedStatusCode) {
        Assert.assertTrue(
            "Response status code is not " + expectedStatusCode + ", but it is " + response.getStatusCode(),
            response.statusCode() == expectedStatusCode
        );
    }

    @Step("Given the POST service body is generated")
    public CaseViewRequestVO generateCaseViewPostRequestBody() {
        CaseViewRequestVO caseViewRequestVO = new CaseViewRequestVO();
        ViewLog viewLog = new ViewLog();
        viewLog.setUserId("3748240");
        viewLog.setCaseRef("1615817621013549");
        viewLog.setCaseJurisdictionId("CMC");
        viewLog.setCaseTypeId("Caveats");
        viewLog.setTimestamp("2021-08-23T22:20:05.023Z");
        caseViewRequestVO.setViewLog(viewLog);
        return caseViewRequestVO;
    }

    @Step("When the POST service is invoked")
    public Response whenThePostServiceIsInvoked(String serviceToken, Object viewLog) {
        return performPostOperation(TestConstants.AUDIT_CASE_VIEW_ENDPOINT, null, null, viewLog, serviceToken);
    }

    @Step("Then a success response is returned")
    public void thenASuccessResposeIsReturned(Response response) {
        Assert.assertTrue(
            "Response status code is not 200, but it is " + response.getStatusCode(),
            response.statusCode() == 200 || response.statusCode() == 201
        );
    }
}
