package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.insights.AppInsights;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseSearchService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.CASE_REF;
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
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.GET_SEARCH_REQUEST_INFO;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.GET_SEARCH_REQUEST_INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.POST_SEARCH_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.laubackend.cases.insights.AppInsightsEvent.POST_SEARCH_REQUEST_INVALID_REQUEST_EXCEPTION;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestSearchParamsConditions;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyRequestSearchParamsAreNotEmpty;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Case search database operations.", description = "This is the Log and Audit "
        + "Back-End API that will audit case searches. "
        + "The API will be invoked by both the CCD (POST) and the LAU front-end service (GET).")
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.UnnecessaryAnnotationValueElement"})
public class CaseSearchController {

    private static final String EXCEPTION_TRACKING_NAME = "exception";

    private final CaseSearchService caseSearchService;

    private final AppInsights appInsights;

    @Operation(tags = "POST end-points", summary = "Save case search audits", description = "This operation will "
            + "persist CCD case search entries which are posted in the request. Single CaseSearch per request will "
            + "be stored in the database.")

    @ApiResponse(
        responseCode = "201",
        description = "Created SearchLog case response - includes caseSearchId from DB.",
        content = { @Content(schema = @Schema(implementation = CaseSearchPostResponse.class))})
    @ApiResponse(
        responseCode = "400",
        description = "Invalid case search",
        content = { @Content(schema = @Schema(implementation = CaseSearchPostResponse.class))})
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = { @Content(schema = @Schema(implementation = CaseSearchPostResponse.class))})
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = { @Content(schema = @Schema(implementation = CaseSearchPostResponse.class))})
    @PostMapping(
        path = "/audit/caseSearch",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CaseSearchPostResponse> saveCaseSearch(
            @RequestBody final CaseSearchPostRequest caseSearchPostRequest,
            @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
            @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) final String serviceAuthToken) {
        try {
            verifyRequestSearchParamsAreNotEmpty(caseSearchPostRequest);
            verifyRequestSearchParamsConditions(caseSearchPostRequest.getSearchLog());

            final CaseSearchPostResponse caseSearchAudit = caseSearchService.saveCaseSearch(caseSearchPostRequest);

            return new ResponseEntity<>(caseSearchAudit, CREATED);
        } catch (final InvalidRequestException invalidRequestException) {
            log.error("saveCaseSearch API call failed due to error - {}",
                    invalidRequestException.getMessage(),
                    invalidRequestException
            );
            appInsights.trackEvent(POST_SEARCH_REQUEST_INVALID_REQUEST_EXCEPTION.toString(), appInsights.trackingMap(
                EXCEPTION_TRACKING_NAME, invalidRequestException.getMessage()));
            return new ResponseEntity<>(null, BAD_REQUEST);
        } catch (final Exception exception) {
            log.error("saveCaseSearch API call failed due to error - {}",
                    exception.getMessage(),
                    exception
            );
            appInsights.trackEvent(POST_SEARCH_REQUEST_EXCEPTION.toString(), appInsights.trackingMap(
                EXCEPTION_TRACKING_NAME, exception.getMessage()));
            return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(tags = "GET end-points", summary = "Retrieve case search audits", description = "This operation will "
            + "query and return a list of case searches based on the search conditions provided in the URL path.")
    @ApiResponse(
        responseCode = "200",
        description = "Request executed successfully. Response contains of case search logs",
        content = { @Content(schema = @Schema(implementation = CaseSearchGetResponse.class))})
    @ApiResponse(
        responseCode = "400",
        description = "Missing userId, caseRef, startTimestamp or endTimestamp parameters.",
        content = { @Content(schema = @Schema(implementation = CaseSearchGetResponse.class))})
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = { @Content(schema = @Schema(implementation = CaseSearchGetResponse.class))})
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = { @Content(schema = @Schema(implementation = CaseSearchGetResponse.class))})
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = { @Content(schema = @Schema(implementation = CaseSearchGetResponse.class))})
    @GetMapping(
            path = "/audit/caseSearch",
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    @SuppressWarnings({"PMD.UseObjectForClearerAPI"})
    public ResponseEntity<CaseSearchGetResponse> getCaseSearch(
            @Parameter(name = "Authorization", example = "Bearer eyJ0eXAiOiJK.........")
            @RequestHeader(value = AUTHORISATION_HEADER) String authToken,
            @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
            @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String serviceAuthToken,
            @Parameter(name = "User ID", example = "3748238")
            @RequestParam(value = USER_ID, required = false) final String userId,
            @Parameter(name = "Case Reference ID", example = "1615817621013640")
            @RequestParam(value = CASE_REF, required = false) final String caseRef,
            @Parameter(name = "Start Timestamp", example = "2021-06-23T22:20:05")
            @RequestParam(value = START_TIME, required = false) final String startTime,
            @Parameter(name = "End Timestamp", example = "2021-08-23T22:20:05")
            @RequestParam(value = END_TIME, required = false) final String endTime,
            @Parameter(name = "Size", example = "500")
            @RequestParam(value = SIZE, required = false) final String size,
            @Parameter(name = "Page", example = "1")
            @RequestParam(value = PAGE, required = false) final String page) {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(userId,
                    caseRef,
                    startTime,
                    endTime,
                    size,
                    page);
            final long timeStart = System.currentTimeMillis();
            verifyRequestSearchParamsAreNotEmpty(inputParamsHolder);
            verifyRequestSearchParamsConditions(inputParamsHolder);

            final CaseSearchGetResponse caseSearch = caseSearchService.getCaseSearch(inputParamsHolder);
            final long timeEnd = System.currentTimeMillis();
            final String report = (timeEnd - timeStart) > PERF_TOLERANCE_THRESHOLD_MS
                ? PERF_THRESHOLD_MESSAGE_ABOVE : PERF_THRESHOLD_MESSAGE_BELOW;
            appInsights.trackEvent(GET_SEARCH_REQUEST_INFO.toString(), appInsights.trackingMap(
                "GET /audit/caseSearch", report));
            return new ResponseEntity<>(caseSearch, OK);
        } catch (final InvalidRequestException invalidRequestException) {
            log.error(
                    "getCaseView API call failed due to error - {}",
                    invalidRequestException.getMessage(),
                    invalidRequestException
            );
            appInsights.trackEvent(GET_SEARCH_REQUEST_INVALID_REQUEST_EXCEPTION.toString(), appInsights.trackingMap(
                EXCEPTION_TRACKING_NAME, invalidRequestException.getMessage()));
            return new ResponseEntity<>(null, BAD_REQUEST);
        }
    }
}
