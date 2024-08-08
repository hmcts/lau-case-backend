package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseActionResponseVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_CASE_ACTION_ENDPOINT;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.JURISDICTIONS_CASE_TYPES_ENDPOINT;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.SUCCESS;

@SuppressWarnings("PMD.TooManyMethods")
public class CaseActionGetApiSteps extends BaseSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaseActionGetApiSteps.class);

    @Step("Given a valid service token is generated")
    public String givenAValidServiceTokenIsGenerated() {
        return authorizationHeaderHelper.getServiceToken();
    }

    @Step("And valid Authorization token is generated")
    public String validAuthorizationTokenIsGenerated() throws JSONException {
        return authorizationHeaderHelper.getAuthorizationToken();
    }

    @Step("When valid params are supplied for Get CaseAction API")
    public Map<String, String> givenValidParamsAreSuppliedForGetCaseAction() {
        HashMap<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("userId", "3748240");
        queryParamMap.put("caseAction", "VIEW");
        queryParamMap.put("caseRef", "1615817621013549");
        queryParamMap.put("caseJurisdictionId", "CMC");
        queryParamMap.put("caseTypeId", "CAVEATS");
        queryParamMap.put("startTimestamp", "2021-08-22T22:19:05");
        queryParamMap.put("endTimestamp", "2021-08-23T22:20:06");
        return queryParamMap;
    }

    @Step("When the CaseAction GET service is invoked with the valid params")
    public Response whenTheGetCaseActionServiceIsInvokedWithTheGivenParams(String serviceToken,
                                                                           String authorizationToken,
                                                                           Map<String, String> queryParamMap) {
        return performGetOperation(AUDIT_CASE_ACTION_ENDPOINT,
                null, queryParamMap, serviceToken, authorizationToken
        );
    }

    @Step("When the CaseAction GET jurisidictionsCaseTypes service is invoked")
    public Response whenTheGetJurisdictionsCaseTypesServiceIsInvoked(String serviceToken, String authorizationToken) {
        return performGetOperation(JURISDICTIONS_CASE_TYPES_ENDPOINT, null, null, serviceToken, authorizationToken);
    }

    @Step("Then at least one record number should exist")
    public void thenAtLeastOneRecordNumberShouldExist(Response response) {
        response.then().assertThat().body("startRecordNumber", Matchers.is(Matchers.greaterThanOrEqualTo(1)));
    }

    @Step("Then the GET CaseAction response params match the input")
    public String thenTheGetCaseActionResponseParamsMatchesTheInput(Map<String, String> inputQueryParamMap,
                                                                    CaseActionResponseVO caseActionResponseVO) {
        int startRecordNumber = caseActionResponseVO.getStartRecordNumber();
        Assert.assertTrue("Start record number should be greater than 0",
                          startRecordNumber > 0);
        List<ActionLog> actionLogList = caseActionResponseVO.getActionLog();
        ActionLog actionLogObj = actionLogList == null || actionLogList.get(0) == null
                ? new ActionLog() : actionLogList.get(0);
        for (String queryParam : inputQueryParamMap.keySet()) {

            if ("userId".equals(queryParam)) {
                String userId = actionLogObj.getUserId();
                Assert.assertEquals(
                        "User Id is missing in the response",
                        inputQueryParamMap.get(queryParam), userId
                );
            } else if ("caseRef".equals(queryParam)) {
                String caseRef = actionLogObj.getCaseRef();
                Assert.assertEquals(
                        "caseRef is missing in the response",
                        inputQueryParamMap.get(queryParam), caseRef
                );

            } else if ("caseJurisdictionId".equals(queryParam)) {
                String caseJurisdictionId = actionLogObj.getCaseJurisdictionId();
                Assert.assertEquals(
                        "caseJurisdictionId is missing in the response",
                        inputQueryParamMap.get(queryParam), caseJurisdictionId
                );

            } else if ("caseTypeId".equals(queryParam)) {
                String caseTypeId = actionLogObj.getCaseTypeId();
                Assert.assertEquals(
                        "caseTypeId is missing in the response",
                        inputQueryParamMap.get(queryParam), caseTypeId
                );

            } else if ("caseAction".equals(queryParam)) {
                String caseAction = actionLogObj.getCaseAction();
                Assert.assertEquals(
                        "caseAction is missing in the response",
                        inputQueryParamMap.get(queryParam), caseAction
                );

            }
        }
        return SUCCESS;
    }


    @Step("Then the GET CaseAction response date range matches the input")
    public String thenTheGetCaseViewResponseDateRangeMatchesTheInput(Map<String, String> inputQueryParamMap,
                                                                     CaseActionResponseVO caseActionResponseVO)
            throws ParseException {
        List<ActionLog> actionLogList = caseActionResponseVO.getActionLog();
        ActionLog actionLogObject = actionLogList.getFirst();
        String timeStampResponse = actionLogObject.getTimestamp();
        String timeStampStartInputParam = inputQueryParamMap.get("startTimestamp");
        String timeStampEndInputParam = inputQueryParamMap.get("endTimestamp");

        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
        String responseDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Date inputStartTimestamp = new SimpleDateFormat(dateFormat, Locale.UK).parse(timeStampStartInputParam);
        Date inputEndTimestamp = new SimpleDateFormat(dateFormat, Locale.UK).parse(timeStampEndInputParam);
        Date responseTimestamp = new SimpleDateFormat(responseDateFormat, Locale.UK).parse(timeStampResponse);

        LOGGER.info("Input start date : {}", inputStartTimestamp.getTime());
        LOGGER.info("Input end date : {}", inputEndTimestamp.getTime());
        LOGGER.info("Output date : {}", responseTimestamp.getTime());

        Assert.assertTrue("ResponseDate is not between startDate and endDate",
                          responseTimestamp.after(inputStartTimestamp)
                              && responseTimestamp.before(inputEndTimestamp)
                              || responseTimestamp.getTime() == inputStartTimestamp.getTime()
                              || responseTimestamp.getTime() == inputEndTimestamp.getTime()
        );
        return SUCCESS;
    }

    @Step("Given empty params values are supplied for the GET CaseAction API")
    public Map<String, String> givenEmptyParamsAreSuppliedForGetCaseAction() {
        Map<String, String> queryParamMap = new ConcurrentHashMap<>();
        queryParamMap.put("userId", "");
        queryParamMap.put("caseRef", "");
        queryParamMap.put("caseJurisdictionId", "");
        queryParamMap.put("caseTypeId", "");
        queryParamMap.put("startTimestamp", "");
        queryParamMap.put("endTimestamp", "");
        return queryParamMap;
    }

    @Step("Given the invalid service authorization token is generated")
    public String givenTheInvalidServiceTokenIsGenerated() {
        String authServiceToken = givenAValidServiceTokenIsGenerated();
        return authServiceToken + "abc";
    }

    @Step("Given the invalid authorization token is generated")
    public String givenTheInvalidAuthorizationTokenIsGenerated() throws JSONException {
        String authServiceToken = validAuthorizationTokenIsGenerated();
        return authServiceToken + "abc";
    }

    @Step("Then bad response is returned")
    public String thenBadResponseIsReturned(Response response, int expectedStatusCode) {
        Assert.assertEquals(
                "Response status code is not " + expectedStatusCode + ", but it is " + response.getStatusCode(),
                expectedStatusCode,response.statusCode()
        );
        return SUCCESS;
    }

    @Step("Then a success response is returned")
    public String thenASuccessResposeIsReturned(Response response) {
        Assert.assertTrue(
                "Response status code is not 200, but it is " + response.getStatusCode(),
                response.statusCode() == 200 || response.statusCode() == 201
        );
        return SUCCESS;
    }

}
