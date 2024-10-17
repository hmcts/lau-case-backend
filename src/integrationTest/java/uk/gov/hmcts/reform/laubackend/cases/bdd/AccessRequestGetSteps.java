package uk.gov.hmcts.reform.laubackend.cases.bdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestAction;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.response.AccessRequestGetResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static uk.gov.hmcts.reform.laubackend.cases.helper.AccessRequestHelper.mapToAccessRequestPostRequestBody;

public class AccessRequestGetSteps extends AbstractSteps {

    private static boolean posted;
    private final Gson jsonReader = new Gson();
    private String responseBody;

    @When("I POST multiple Access Request records to {string} endpoint once using data:")
    public void postAcceessRequestRecordsToEndpoint(String path, List<Map<String, String>> data) {
        if (!posted) {
            String endpoint = baseUrl() + path;
            data.forEach(entry -> {
                String requestBody = mapToAccessRequestPostRequestBody(entry);
                final Response response = restHelper.postObjectAsString(requestBody, endpoint);
                assertThat(response.getStatusCode()).isEqualTo(CREATED.value());
            });
            posted = true;
        }
    }

    @When("I GET {string} using params:")
    public void getAccessRequestUsingParams(String path, Map<String, String> queryParams) {
        String endpoint = baseUrl() + path;
        final Response response = restHelper.getResponse(endpoint, queryParams);
        responseBody = response.getBody().asString();
    }

    @Then("the list of accessRequest records returned is \\(expected total {int}):")
    public void listOfAccessRequestRecordsReturned(int expectedNumber, List<Map<String, String>> expectedRecords)
        throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        final AccessRequestGetResponse response = objectMapper.readValue(
            responseBody, AccessRequestGetResponse.class);

        assertThat(response.getTotalNumberOfRecords()).isEqualTo(expectedNumber);
        List<AccessRequestLog> logs = response.getAccessLog();
        assertThat(logs).hasSameSizeAs(expectedRecords);
        for (int i = 0; i < expectedRecords.size(); i++) {
            AccessRequestType expectedRequestType =
                AccessRequestType.valueOf(expectedRecords.get(i).get("requestType"));
            AccessRequestAction expectedAction = AccessRequestAction
                .getAccessRequestAction(expectedRecords.get(i).get("action"));
            assertThat(logs.get(i).getRequestType()).isEqualTo(expectedRequestType);
            assertThat(logs.get(i).getUserId()).isEqualTo(expectedRecords.get(i).get("userId"));
            assertThat(logs.get(i).getCaseRef()).isEqualTo(expectedRecords.get(i).get("caseRef"));
            assertThat(logs.get(i).getReason()).isEqualTo(expectedRecords.get(i).get("reason"));
            assertThat(logs.get(i).getAction()).isEqualTo(expectedAction);
            assertThat(logs.get(i).getRequestEnd()).isEqualTo(expectedRecords.get(i).get("requestEndTimestamp"));
            assertThat(logs.get(i).getTimestamp()).isEqualTo(expectedRecords.get(i).get("timestamp"));
        }
    }

}
