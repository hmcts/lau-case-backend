package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseViewRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseViewResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CaseViewSteps extends BaseSteps {

    private static final Logger logger = LoggerFactory.getLogger(CaseViewSteps.class);


    @Step("Given a valid service token is generated")
    public String givenAValidServiceTokenIsGenerated() {
        return getServiceToken(TestConstants.S2S_NAME);
    }

    @Step("When valid params are supplied for Get CaseView API")
    public Map<String, String> givenValidParamsAreSuppliedForGetCaseView() {
        HashMap<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("userId", "3748240");
        queryParamMap.put("caseRef", "1615817621013549");
        queryParamMap.put("caseJurisdictionId", "CMC");
        queryParamMap.put("caseTypeId", "Caveats");
        queryParamMap.put("starttimestamp", "2021-08-22T22:19:05");
        queryParamMap.put("endtimestamp", "2021-08-23T22:20:06");
        return queryParamMap;
    }

    @Step("When the CaseView GET service is invoked with the valid params")
    public Response whenTheGetCaseViewServiceIsInvokedWithTheGivenParams(String serviceToken,
                                                                         Map<String, String> queryParamMap) {
        return performGetOperation(TestConstants.AUDIT_CASE_VIEW_ENDPOINT,
                                   null, queryParamMap, serviceToken);
    }

    @Step("Then at least one record number should exist")
    public void thenAtLeastOneRecordNumberShouldExist(Response response) {
        response.then().assertThat().body("startRecordNumber", Matchers.is(Matchers.greaterThanOrEqualTo(1)));
    }

    @Step("Then the GET CaseView response params match the input")
    public String thenTheGetCaseViewResponseParamsMatchesTheInput(Map<String, String> inputQueryParamMap,
                                                                CaseViewResponseVO caseViewResponseVO) {
        int startRecordNumber = caseViewResponseVO.getStartRecordNumber();
        Assert.assertTrue(startRecordNumber > 0);
        List<ViewLog> viewLogList = caseViewResponseVO.getViewLog();
        ViewLog viewLogObj = viewLogList.get(0);
        for (String queryParam : inputQueryParamMap.keySet()) {

            if ("userId".equals(queryParam)) {
                Assert.assertEquals(
                    "User Id is missing in the response",
                    inputQueryParamMap.get(queryParam),
                    viewLogObj.getUserId()
                );
            } else if ("caseRef".equals(queryParam)) {
                Assert.assertEquals(
                    "caseRef is missing in the response",
                    inputQueryParamMap.get(queryParam),
                    viewLogObj.getCaseRef()
                );

            } else if ("caseJurisdictionId".equals(queryParam)) {
                Assert.assertEquals(
                    "caseJurisdictionId is missing in the response",
                    inputQueryParamMap.get(queryParam),
                    viewLogObj.getCaseJurisdictionId()
                );

            } else if ("caseTypeId".equals(queryParam)) {
                Assert.assertEquals(
                    "caseTypeId is missing in the response",
                    inputQueryParamMap.get(queryParam),
                    viewLogObj.getCaseTypeId()
                );

            }
        }
        return TestConstants.SUCCESS;
    }

    @Step("Then the GET CaseView response date range matches the input")
    public String thenTheGetCaseViewResponseDateRangeMatchesTheInput(Map<String, String> inputQueryParamMap,
                                                                   CaseViewResponseVO caseViewResponseVO) throws ParseException {

            List<ViewLog> viewLogList = caseViewResponseVO.getViewLog();
            ViewLog viewLogObject = viewLogList.get(0);
            String timeStampResponse = viewLogObject.getTimestamp();
            String timeStampStartInputParam = inputQueryParamMap.get("starttimestamp");
            String timeStampEndInputParam = inputQueryParamMap.get("endtimestamp");

            String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
            String responseDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            Date inputStartTimestamp = new SimpleDateFormat(dateFormat, Locale.UK).parse(timeStampStartInputParam);
            Date inputEndTimestamp = new SimpleDateFormat(dateFormat, Locale.UK).parse(timeStampEndInputParam);
            Date responseTimestamp = new SimpleDateFormat(responseDateFormat, Locale.UK).parse(timeStampResponse);

            logger.info("Input start date : " + inputStartTimestamp.getTime());
            logger.info("Input end date : " + inputEndTimestamp.getTime());
            logger.info("Output date : " + responseTimestamp.getTime());

            Assert.assertTrue(responseTimestamp.getTime() == inputStartTimestamp.getTime()
                                  || responseTimestamp.getTime() == inputEndTimestamp.getTime()
                                  || (responseTimestamp.after(inputStartTimestamp) && responseTimestamp.before(
                inputEndTimestamp))
            );
        return TestConstants.SUCCESS;
    }

    @Step("Given empty params values are supplied for the GET CaseView API")
    public Map<String, String> givenEmptyParamsAreSuppliedForGetCaseView() {
        Map<String, String> queryParamMap = new ConcurrentHashMap<>();
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
        return authServiceToken + "abc";
    }

    @Step("Then 401 response is returned")
    public String thenBadResponseForServiceAuthorizationIsReturned(Response response, int expectedStatusCode) {
        Assert.assertTrue(
            "Response status code is not " + expectedStatusCode + ", but it is " + response.getStatusCode(),
            response.statusCode() == expectedStatusCode
        );
        return TestConstants.SUCCESS;
    }

    @Step("Given the POST service body is generated")
    public CaseViewRequestVO generateCaseViewPostRequestBody() {
        ViewLog viewLog = new ViewLog();
        viewLog.setUserId("3748240");
        viewLog.setCaseRef("1615817621013549");
        viewLog.setCaseJurisdictionId("CMC");
        viewLog.setCaseTypeId("Caveats");
        viewLog.setTimestamp("2021-08-23T22:20:05.023Z");
        CaseViewRequestVO caseViewRequestVO = new CaseViewRequestVO();
        caseViewRequestVO.setViewLog(viewLog);
        return caseViewRequestVO;
    }

    @Step("When the POST service is invoked")
    public Response whenThePostServiceIsInvoked(String serviceToken, Object viewLog) throws JsonProcessingException {
        return performPostOperation(TestConstants.AUDIT_CASE_VIEW_ENDPOINT, null, null, viewLog, serviceToken);
    }

    @Step("Then a success response is returned")
    public String thenASuccessResposeIsReturned(Response response) {
        Assert.assertTrue(
            "Response status code is not 200, but it is " + response.getStatusCode(),
            response.statusCode() == 200 || response.statusCode() == 201
        );
        return TestConstants.SUCCESS;
    }
}
