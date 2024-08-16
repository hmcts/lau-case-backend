package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessRequestPostApiSteps extends BaseSteps {

    public String generateAccessRequestPostRequestBody() {
        JSONObject jsonRequestBody = new JSONObject();
        JSONObject jsonAccessLog = new JSONObject();
        String uuid = UUID.randomUUID().toString();
        String caseRef = RandomStringUtils.random(16, false, true);

        try {
            jsonAccessLog.put("requestType", "CHALLENGED");
            jsonAccessLog.put("userId", "user-" + uuid);
            jsonAccessLog.put("caseRef", caseRef);
            jsonAccessLog.put("reason", "functional test - shouldn't stay in DB");
            jsonAccessLog.put("action", "APPROVED");
            jsonAccessLog.put("timeLimit", Instant.now().toString());
            jsonAccessLog.put("timestamp", Instant.now().toString());
            jsonRequestBody.put("accessLog", jsonAccessLog);
            return jsonRequestBody.toString();
        } catch (JSONException je) {
            throw new RuntimeException("Error while creating JSON object", je);
        }
    }

    public void assertSuccessResponse(Response response, int expectedStatusCode) {
        String failMsg = "Expected status code to be " + expectedStatusCode + " but was " + response.getStatusCode();

        assertThat(response.statusCode())
            .withFailMessage(failMsg)
            .isEqualTo(expectedStatusCode);
    }
}
