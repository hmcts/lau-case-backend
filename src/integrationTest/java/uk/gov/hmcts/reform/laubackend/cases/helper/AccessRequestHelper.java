package uk.gov.hmcts.reform.laubackend.cases.helper;

import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestAction;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestPostRequest;

import java.util.Map;

public class AccessRequestHelper {

    private AccessRequestHelper() {
    }

    public static AccessRequestPostRequest getAccessRequestPostRequest() {
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(AccessRequestType.CHALLENGED)
            .userId("user-id")
            .caseRef("1234567890123456")
            .reason("reason")
            .action(AccessRequestAction.APPROVED)
            .requestStart("2021-08-01T09:23:00.000Z")
            .requestEnd("2021-08-02T09:23:00.000Z")
            .timestamp("2021-08-01T14:34:00.000Z")
            .build();

        return new AccessRequestPostRequest(accessRequestLog);
    }

    public static String mapToAccessRequestPostRequestBody(Map<String, String> dataMap) {
        JSONObject jsonRequestBody = new JSONObject();
        JSONObject jsonAccessLog = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                jsonAccessLog.put(entry.getKey(), entry.getValue());
            }
            jsonRequestBody.put("accessLog", jsonAccessLog);
            return jsonRequestBody.toString();
        } catch (JSONException je) {
            throw new RuntimeException("Error while creating JSON object", je);
        }
    }
}
