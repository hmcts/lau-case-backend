package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.utils.TestConstants.AUDIT_ACCESS_REQUEST_ENDPOINT;

public class AccessRequestGetApiSteps extends BaseSteps {

    private static final String CASE_REF = "caseRef";
    private static final String REQUEST_TYPE = "requestType";
    private static final String USER_ID = "userId";

    @Step("Perform get request")
    public Response whenTheGetAccessRequestServiceIsInvokedWithTheGivenParams(
        String svcToken, String authToken, Map<String, String> queryParams) {

        return performGetOperation(AUDIT_ACCESS_REQUEST_ENDPOINT,null, queryParams, svcToken, authToken);
    }

    @Step("Create request query params")
    public Map<String, String> createRequestQueryParams(String responseBody) throws JSONException {
        JSONObject requestBody = new JSONObject(responseBody);
        JSONObject accessLog = requestBody.getJSONObject("accessLog");

        String startTimestamp = Instant.now().minus(1, ChronoUnit.DAYS).toString()
            .replace("Z", "");
        String endTimestamp = Instant.now().plus(1, ChronoUnit.DAYS).toString()
            .replace("Z", "");

        return Map.of(
            USER_ID, accessLog.getString(USER_ID),
            CASE_REF, accessLog.getString(CASE_REF),
            REQUEST_TYPE, accessLog.getString(REQUEST_TYPE),
            "startTimestamp", URLEncoder.encode(startTimestamp, StandardCharsets.UTF_8),
            "endTimestamp", URLEncoder.encode(endTimestamp, StandardCharsets.UTF_8),
            "page", "1",
            "size", "500"
        );
    }

    public void assertSuccessResponse(Response response, JSONObject expected, int expectedStatusCode)
        throws JSONException {

        String failMsg = "Expected status code to be " + expectedStatusCode + " but was " + response.getStatusCode();
        assertThat(response.statusCode())
            .withFailMessage(failMsg)
            .isEqualTo(expectedStatusCode);

        JSONObject responseBody = new JSONObject(response.getBody().asString());
        assertThat(responseBody.getBoolean("moreRecords")).isFalse();
        assertThat(responseBody.getInt("startRecordNumber")).isEqualTo(1);
        assertThat(responseBody.getInt("totalNumberOfRecords")).isEqualTo(1);

        JSONArray accessLogs = responseBody.getJSONArray("accessLog");
        assertThat(accessLogs.length()).isEqualTo(1);
        JSONObject accessLog = accessLogs.getJSONObject(0);

        assertThat(accessLog.getString(USER_ID)).isEqualTo(expected.getString(USER_ID));
        assertThat(accessLog.getString(CASE_REF)).isEqualTo(expected.getString(CASE_REF));
        assertThat(accessLog.getString(REQUEST_TYPE)).isEqualTo(expected.getString(REQUEST_TYPE));
        assertThat(accessLog.getString("reason")).isEqualTo(expected.getString("reason"));
        assertThat(accessLog.getString("action")).isEqualTo(expected.getString("action"));

        assertThat(truncateMillisFromTimestamp(accessLog.getString("timeLimit")))
            .isEqualTo(truncateMillisFromTimestamp(expected.getString("timeLimit")));

        assertThat(truncateMillisFromTimestamp(accessLog.getString("timestamp")))
            .isEqualTo(truncateMillisFromTimestamp(expected.getString("timestamp")));
    }

    private String truncateMillisFromTimestamp(String timestamp) {
        return timestamp.substring(0, timestamp.lastIndexOf(".")) + "Z";
    }

}
