package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.Assert;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseSearchGetResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.SearchLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_CASE_SEARCH_ENDPOINT;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.SUCCESS;

@SuppressWarnings("PMD.TooManyMethods")
public class CaseSearchGetApiSteps extends BaseSteps {

    @Step("Given a valid service token is generated")
    public String givenAValidServiceTokenIsGenerated() {
        return authorizationHeaderHelper.getServiceToken();
    }

    @Step("And valid Authorization token is generated")
    public String validAuthorizationTokenIsGenerated() throws JSONException {
        return authorizationHeaderHelper.getAuthorizationToken();
    }

    @Step("When valid params are supplied for Get CaseSearch API")
    public Map<String, String> givenValidParamsAreSuppliedForGetCaseSearchApi() {
        HashMap<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("userId", "123455");
        queryParamMap.put("caseRef", "1589282126569940");
        queryParamMap.put("startTimestamp", "2021-10-18T13:41:05");
        queryParamMap.put("endTimestamp", "2021-10-20T13:45:05");
        return queryParamMap;
    }

    @Step("When the caseSearch GET service is invoked with the valid params")
    public Response whenTheGetCaseSearchServiceIsInvokedWithTheGivenParams(String serviceToken,
                                                                           String authorizationToken,
                                                                           Map<String, String> queryParamMap) {
        return performGetOperation(AUDIT_CASE_SEARCH_ENDPOINT,
                null, queryParamMap, serviceToken, authorizationToken
        );
    }

    @Step("Then a success response is returned")
    public String thenASuccessResposeIsReturned(Response response) {
        Assert.assertTrue(
                "Response status code is not 200, but it is " + response.getStatusCode(),
                response.statusCode() == 200 || response.statusCode() == 201
        );
        return SUCCESS;
    }

    @Step("Then at least one record number should exist")
    public void thenAtLeastOneRecordNumberShouldExist(Response response) {
        response.then().assertThat().body("startRecordNumber", Matchers.is(Matchers.greaterThanOrEqualTo(1)));
    }

    @Step("Then the GET CaseSearch response params match the input")
    public String thenTheGetCaseSearchResponseParamsMatchesTheInput(Map<String, String> inputQueryParamMap,
                                                                    CaseSearchGetResponseVO caseSearchGetResponseVO) {
        int startRecordNumber = caseSearchGetResponseVO.getStartRecordNumber();
        Assert.assertTrue("Start record number should be greater than 0",
                          startRecordNumber > 0);
        List<SearchLog> searchLogList = caseSearchGetResponseVO.getSearchLog();
        SearchLog searchLogObj = searchLogList == null || searchLogList.get(0) == null
                ? new SearchLog() : searchLogList.get(0);
        for (String queryParam : inputQueryParamMap.keySet()) {

            if ("userId".equals(queryParam)) {
                String userId = searchLogObj.getUserId();
                Assert.assertEquals(
                        "UserId is missing in the response",
                        inputQueryParamMap.get(queryParam), userId
                );
            } else if ("caseRef".equals(queryParam)) {
                List<String> caseRef = searchLogObj.getcaseRefs();
                Assert.assertTrue(
                        "caseRef is missing in the response",
                        caseRef.contains(inputQueryParamMap.get(queryParam))
                );

            }
        }
        return SUCCESS;
    }


    @Step("Then the GET CaseSearch response date range matches the input")
    public String thenTheGetCaseSearchResponseDateRangeMatchesTheInput(Map<String, String> inputQueryParamMap,
                                                                       CaseSearchGetResponseVO caseSearchGetResponseVO)
            throws ParseException {
        List<SearchLog> searchLogList = caseSearchGetResponseVO.getSearchLog();
        SearchLog searchLogObject = searchLogList.get(0);
        String timeStampResponse = searchLogObject.getTimestamp();
        String timeStampStartInputParam = inputQueryParamMap.get("startTimestamp");
        String timeStampEndInputParam = inputQueryParamMap.get("endTimestamp");

        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
        String responseDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Date inputStartTimestamp = new SimpleDateFormat(dateFormat, Locale.UK).parse(timeStampStartInputParam);
        Date inputEndTimestamp = new SimpleDateFormat(dateFormat, Locale.UK).parse(timeStampEndInputParam);
        Date responseTimestamp = new SimpleDateFormat(responseDateFormat, Locale.UK).parse(timeStampResponse);
        Assert.assertTrue("ResponseDate is not between startDate and endDate",
                          responseTimestamp.after(inputStartTimestamp)
                              && responseTimestamp.before(inputEndTimestamp)
                              || responseTimestamp.getTime() == inputStartTimestamp.getTime()
                              || responseTimestamp.getTime() == inputEndTimestamp.getTime()
        );
        return SUCCESS;
    }

    @Step("Given the invalid service authorization token is generated")
    public String givenTheInvalidServiceTokenIsGenerated() {
        String authServiceToken = givenAValidServiceTokenIsGenerated();
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

    @Step("Given empty params values are supplied for the GET CaseSearch API")
    public Map<String, String> givenEmptyParamsAreSuppliedForGetCaseSearchApi() {
        Map<String, String> queryParamMap = new ConcurrentHashMap<>();
        queryParamMap.put("userId", "");
        queryParamMap.put("caseRef", "");
        queryParamMap.put("startTimestamp", "");
        queryParamMap.put("endTimestamp", "");
        return queryParamMap;
    }

    @Step("Given the invalid authorization token is generated")
    public String givenTheInvalidAuthorizationTokenIsGenerated() throws JSONException {
        String authToken = validAuthorizationTokenIsGenerated();
        return authToken + "abc";
    }
}


