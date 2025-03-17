package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class AccessRequestPostApiSteps extends BaseSteps {

    public JSONObject generateAccessRequestPostRequestBodyJson() throws JSONException {
        final JSONObject jsonRequestBody = new JSONObject();
        JSONObject jsonAccessLog = new JSONObject();
        String uuid = UUID.randomUUID().toString();
        String caseRef = RandomStringUtils.secure().next(16, false, true);

        jsonAccessLog.put("requestType", "CHALLENGED");
        jsonAccessLog.put("userId", "user-" + uuid);
        jsonAccessLog.put("caseRef", caseRef);
        jsonAccessLog.put("reason", "functional test - shouldn't stay in DB");
        jsonAccessLog.put("action", "AUTO-APPROVED");
        jsonAccessLog.put("requestEndTimestamp", Instant.now().plus(1, ChronoUnit.DAYS).toString());
        jsonAccessLog.put("timestamp", Instant.now().toString());
        jsonRequestBody.put("accessLog", jsonAccessLog);
        return jsonRequestBody;
    }
}
