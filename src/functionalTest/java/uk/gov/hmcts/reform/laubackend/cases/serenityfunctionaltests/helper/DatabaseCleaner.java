package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.helper;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseActionPostResponseVO;
import uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model.CaseSearchPostResponseVO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.config.EnvConfig.API_URL;

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