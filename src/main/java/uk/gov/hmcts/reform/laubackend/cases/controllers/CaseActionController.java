package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.insights.AppInsights;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseActionPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.JurisdictionsCaseTypesResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseActionService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.CASE_ACTION;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.CASE_JURISDICTION_ID;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.CASE_REF;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.END_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.PAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.SIZE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.START_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.USER_ID;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.PERF_THRESHOLD_MESSAGE_ABOVE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.PERF_THRESHOLD_MESSAGE_BELOW;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.PERF_TOLERANCE_THRESHOLD_MS;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.GET_ACTIVITY_REQUEST_INFO;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.GET_ACTIVITY_REQUEST_INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.POST_ACTIVITY_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.POST_ACTIVITY_REQUEST_INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestActionParamsConditions;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyRequestActionParamsAreNotEmpty;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Case action database operations.", description = "This is the Log and Audit "
        + "Back-End API that will audit case actions. "
        + "The API will be invoked by both the CCD (POST) and the LAU front-end service (GET).")
@SuppressWarnings({"PMD.ExcessiveImports","PMD.UnnecessaryAnnotationValueElement","PMD.ExcessiveParameterList"})
public final class CaseActionController {

    private final CaseActionService caseActionService;

    private final AppInsights appInsights;

    private static final String EXCEPTION = "exception";

    @Operation(tags = "POST end-points", summary = "Save case action audits", description = "This operation will "
            + "persist CCD case action entries which are posted in the request. Single CaseAction per request will "
            + "be stored in the database.")
    @ApiResponse(responseCode = "201", description = "Created actionLog case - includes caseActionId from DB.")
    @ApiResponse(responseCode = "400", description = "Invalid case action")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping(
        path = "/audit/caseAction",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CaseActionPostResponse> saveCaseAction(
            @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
            @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String serviceAuthToken,
            @RequestBody final CaseActionPostRequest caseActionPostRequest) {
        try {
            verifyRequestActionParamsAreNotEmpty(caseActionPostRequest.getActionLog());
            verifyRequestActionParamsConditions(caseActionPostRequest.getActionLog());

            final CaseActionPostResponse caseActionPostResponse = caseActionService
                    .saveCaseAction(caseActionPostRequest.getActionLog());

            return new ResponseEntity<>(caseActionPostResponse, CREATED);
        } catch (final InvalidRequestException invalidRequestException) {
            log.error("saveCaseAction API call failed due to error - {}",
                    invalidRequestException.getMessage(),
                    invalidRequestException
            );
            appInsights.trackEvent(POST_ACTIVITY_REQUEST_INVALID_REQUEST_EXCEPTION.toString(), appInsights.trackingMap(
                EXCEPTION, invalidRequestException.getMessage()));
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (final Exception exception) {
            log.error("saveCaseAction API call failed due to error - {}",
                    exception.getMessage(),
                    exception
            );
            appInsights.trackEvent(POST_ACTIVITY_REQUEST_EXCEPTION.toString(), appInsights.trackingMap(
                EXCEPTION, exception.getMessage()));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(tags = "GET end-points", summary = "Retrieve case action audits", description = "This operation will "
            + "query and return a list of case actions based on the search conditions provided in the URL path.")
    @ApiResponse(
        responseCode = "200",
        description = "Request executed successfully. Response contains of case view logs")
    @ApiResponse(
        responseCode = "400",
        description =
            "Missing userId, caseTypeId, caseJurisdictionId, "
                + "caseRef, startTimestamp or endTimestamp parameters.")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping(path = "/audit/caseAction", produces = APPLICATION_JSON_VALUE)
    @SuppressWarnings({"PMD.UseObjectForClearerAPI"})
    public ResponseEntity<CaseActionGetResponse> getCaseAction(
            @Parameter(name = "Authorization", example = "Bearer eyJ0eXAiOiJK.........")
            @RequestHeader(value = AUTHORISATION_HEADER) String authToken,
            @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
            @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String serviceAuthToken,
            @Parameter(name = "User ID", example = "3748238")
            @RequestParam(value = USER_ID, required = false) final String userId,
            @Parameter(name = "Case Reference ID", example = "1615817621013640")
            @RequestParam(value = CASE_REF, required = false) final String caseRef,
            @Parameter(name = "Case Type ID", example = "GrantOfRepresentation")
            @RequestParam(value = CASE_TYPE_ID, required = false) final String caseTypeId,
            @Parameter(name = "Case Action", example = "VIEW")
            @RequestParam(value = CASE_ACTION, required = false) final String caseAction,
            @Parameter(name = "Case Jurisdiction ID", example = "PROBATE")
            @RequestParam(value = CASE_JURISDICTION_ID, required = false) final String caseJurisdictionId,
            @Parameter(name = "Start Timestamp", example = "2021-06-23T22:20:05")
            @RequestParam(value = START_TIME, required = false) final String startTime,
            @Parameter(name = "End Timestamp", example = "2021-08-23T22:20:05")
            @RequestParam(value = END_TIME, required = false) final String endTime,
            @Parameter(name = "Size", example = "500")
            @RequestParam(value = SIZE, required = false) final String size,
            @Parameter(name = "Page", example = "1")
            @RequestParam(value = PAGE, required = false) final String page) {
        try {
            final ActionInputParamsHolder inputParamsHolder = new ActionInputParamsHolder(userId,
                    caseRef,
                    caseTypeId,
                    caseAction,
                    caseJurisdictionId,
                    startTime,
                    endTime,
                    size,
                    page);
            final long timeStart = System.currentTimeMillis();
            verifyRequestActionParamsAreNotEmpty(inputParamsHolder);
            verifyRequestActionParamsConditions(inputParamsHolder);

            final CaseActionGetResponse caseView = caseActionService.getCaseView(inputParamsHolder);
            final long timeEnd = System.currentTimeMillis();
            final String report = (timeEnd - timeStart) > PERF_TOLERANCE_THRESHOLD_MS
                ? PERF_THRESHOLD_MESSAGE_ABOVE : PERF_THRESHOLD_MESSAGE_BELOW;
            appInsights.trackEvent(GET_ACTIVITY_REQUEST_INFO.toString(), appInsights.trackingMap(
                "GET /audit/caseAction", report));
            return new ResponseEntity<>(caseView, OK);
        } catch (final InvalidRequestException invalidRequestException) {
            log.error(
                    "getCaseView API call failed due to error - {}",
                    invalidRequestException.getMessage(),
                    invalidRequestException
            );
            appInsights.trackEvent(GET_ACTIVITY_REQUEST_INVALID_REQUEST_EXCEPTION.toString(), appInsights.trackingMap(
                EXCEPTION, invalidRequestException.getMessage()));
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @Operation(
        tags = "GET end-points",
        summary = "Retrieve jurisdictions and case types",
        description = "Endpoint that returns list of jurisdictions and list of case types"
    )
    @ApiResponse(responseCode = "200", description = "Data retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping(path = "/audit/jurisdictionsCaseTypes", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<JurisdictionsCaseTypesResponse> getJurisdictionsCaseTypes(
        @Parameter(name = "Authorization", example = "Bearer eyJ0eXAiOiJK.........")
        @RequestHeader(value = AUTHORISATION_HEADER) String authToken,
        @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
        @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String serviceAuthToken
    ) {
        JurisdictionsCaseTypesResponse response = caseActionService.getJurisdictionsAndCaseTypes();
        return new ResponseEntity<>(response, OK);
    }

}
