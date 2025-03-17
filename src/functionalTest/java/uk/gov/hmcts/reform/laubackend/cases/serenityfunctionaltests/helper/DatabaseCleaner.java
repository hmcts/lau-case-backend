package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.helper;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseActionPostResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseSearchPostResponseVO;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.config.EnvConfig.API_URL;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidThrowingRawExceptionTypes"})
public final class DatabaseCleaner {

    private DatabaseCleaner() {
    }

    public static void deleteRecord(final String endpoint,
                                    final boolean isCaseSearch,
                                    final Response response) throws JSONException {
        final AuthorizationHeaderHelper authorizationHeaderHelper = new AuthorizationHeaderHelper();

        final String queryParamValue = getQueryParamValue(response, isCaseSearch);

        final Response deleteResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(API_URL + endpoint)
                .queryParam(isCaseSearch ? "caseSearchId" : "caseActionId", queryParamValue)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(SERVICE_AUTHORISATION_HEADER, authorizationHeaderHelper.getServiceToken())
                .header(AUTHORISATION_HEADER, authorizationHeaderHelper.getAuthorizationToken())
                .when()
                .delete()
                .andReturn();

        assertThat(deleteResponse.getStatusCode()).isEqualTo(OK.value());
    }

    private static void deleteRecord(String endpoint, Map<String, String> queryParams) throws JSONException {
        final AuthorizationHeaderHelper authorizationHeaderHelper = new AuthorizationHeaderHelper();
        String authToken = authorizationHeaderHelper.getAuthorizationToken();

        final Response deleteResponse = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .baseUri(API_URL + endpoint)
            .queryParams(queryParams)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .header(SERVICE_AUTHORISATION_HEADER, authorizationHeaderHelper.getServiceToken())
            .header(AUTHORISATION_HEADER, authToken)
            .when()
            .delete()
            .andReturn();

        assertThat(deleteResponse.getStatusCode()).isEqualTo(OK.value());
    }

    public static void deleteAccessRequestRecord(String endpoint, Response response) {

        try {
            JSONObject jsonObject = new JSONObject(response.getBody().asString());
            JSONObject accessLog = jsonObject.getJSONObject("accessLog");
            String userId = accessLog.getString("userId");
            String caseRef = accessLog.getString("caseRef");
            deleteRecord(endpoint, Map.of("userId", userId, "caseRef", caseRef));
        } catch (JSONException je) {
            throw new RuntimeException("Error while creating JSON object", je);
        }
    }

    private static String getQueryParamValue(final Response response, final boolean isCaseSearch) {
        final Gson jsonReader = new Gson();

        if (isCaseSearch) {
            final CaseSearchPostResponseVO caseSearchPostResponseVO = jsonReader
                    .fromJson(response.getBody().asString(), CaseSearchPostResponseVO.class);

            return caseSearchPostResponseVO.getSearchLog().getId();
        }
        final CaseActionPostResponseVO caseSearchPostResponseVO = jsonReader
                .fromJson(response.getBody().asString(), CaseActionPostResponseVO.class);

        return caseSearchPostResponseVO.getActionLog().getCaseActionId();
    }
}
