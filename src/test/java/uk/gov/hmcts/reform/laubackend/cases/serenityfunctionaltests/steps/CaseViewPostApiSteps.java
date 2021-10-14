package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseViewRequestVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants;

public class CaseViewPostApiSteps extends BaseSteps {
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

}
