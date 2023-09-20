package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseSearchRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants;

import java.util.ArrayList;
import java.util.List;

public class CaseSearchPostApiSteps extends BaseSteps {

    @Step("Given the POST service body is generated")
    public CaseSearchRequestVO generateCaseSearchPostRequestBody() {
        SearchLog searchLog = new SearchLog();
        searchLog.setUserId("123455");
        List<String> caseRefList = new ArrayList<>();
        caseRefList.add("1589282126569940");
        searchLog.setcaseRefs(caseRefList);
        searchLog.setTimestamp("2021-10-19T13:20:05.023Z");
        CaseSearchRequestVO caseSearchRequestVO = new CaseSearchRequestVO();
        caseSearchRequestVO.setSearchLog(searchLog);
        return caseSearchRequestVO;
    }

    @Step("Given invalid POST service body is generated")
    public CaseSearchRequestVO generateInvalidCaseSearchPostRequestBody() {
        SearchLog searchLog = new SearchLog();
        searchLog.setUserId("123455$");
        List<String> caseRefList = new ArrayList<>();
        caseRefList.add("1589282126569940$");
        searchLog.setcaseRefs(caseRefList);
        searchLog.setTimestamp("2021-10-19T13:20:05.023Z!");
        CaseSearchRequestVO caseSearchRequestVO = new CaseSearchRequestVO();
        caseSearchRequestVO.setSearchLog(searchLog);
        return caseSearchRequestVO;
    }

    @Step("When the POST service is invoked")
    public Response whenThePostServiceIsInvoked(String serviceToken, Object searchLog) throws JsonProcessingException {
        return performPostOperation(TestConstants.AUDIT_CASE_SEARCH_ENDPOINT, null, null, searchLog, serviceToken);
    }

}

