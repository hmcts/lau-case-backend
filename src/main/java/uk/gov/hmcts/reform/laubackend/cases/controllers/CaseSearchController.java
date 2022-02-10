package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
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
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestSearchParamsConditions;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyRequestSearchParamsAreNotEmpty;

@RestController
@Slf4j
@Api(tags = "Case search database operations.", value = "This is the Log and Audit "
        + "Back-End API that will audit case searches. "
        + "The API will be invoked by both the CCD (POST) and the LAU front-end service (GET).")
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.UnnecessaryAnnotationValueElement", "PMD.LawOfDemeter"})
public class CaseSearchController {

    @Autowired
    private CaseSearchService caseSearchService;

    @ApiOperation(tags = "POST end-points", value = "Save case search audits", notes = "This operation will "
            + "persist CCD case search entries which are posted in the request. Single CaseSearch per request will "
            + "be stored in the database.")
    @ApiResponses({
            @ApiResponse(code = 201,
                    message = "Created SearchLog case response - includes caseSearchId from DB.",
                    response = CaseSearchPostResponse.class),
            @ApiResponse(code = 400,
                    message = "Invalid case search",
                    response = CaseSearchPostResponse.class),
            @ApiResponse(code = 403, message = "Forbidden",
                    response = CaseSearchPostResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error",
                    response = CaseSearchPostResponse.class)
    })
    @PostMapping(
            path = "/audit/caseSearch",
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<CaseSearchPostResponse> saveCaseSearch(
            @RequestBody final CaseSearchPostRequest caseSearchPostRequest,
            @ApiParam(value = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
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
            return new ResponseEntity<>(null, BAD_REQUEST);
        } catch (final Exception exception) {
            log.error("saveCaseSearch API call failed due to error - {}",
                    exception.getMessage(),
                    exception
            );
            return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(tags = "GET end-points", value = "Retrieve case search audits", notes = "This operation will "
            + "query and return a list of case searches based on the search conditions provided in the URL path.")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Request executed successfully. Response contains of case search logs",
                    response = CaseSearchGetResponse.class),
            @ApiResponse(code = 400,
                    message =
                            "Missing userId, caseRef, startTimestamp or endTimestamp parameters.",
                    response = CaseSearchGetResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = CaseSearchGetResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = CaseSearchGetResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = CaseSearchGetResponse.class)
    })
    @GetMapping(
            path = "/audit/caseSearch",
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    @SuppressWarnings({"PMD.UseObjectForClearerAPI"})
    @ResponseBody
    public ResponseEntity<CaseSearchGetResponse> getCaseSearch(
            @ApiParam(value = "Authorization", example = "Bearer eyJ0eXAiOiJK.........")
            @RequestHeader(value = AUTHORISATION_HEADER) String authToken,
            @ApiParam(value = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
            @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String serviceAuthToken,
            @ApiParam(value = "User ID", example = "3748238")
            @RequestParam(value = USER_ID, required = false) final String userId,
            @ApiParam(value = "Case Reference ID", example = "1615817621013640")
            @RequestParam(value = CASE_REF, required = false) final String caseRef,
            @ApiParam(value = "Start Timestamp", example = "2021-06-23T22:20:05")
            @RequestParam(value = START_TIME, required = false) final String startTime,
            @ApiParam(value = "End Timestamp", example = "2021-08-23T22:20:05")
            @RequestParam(value = END_TIME, required = false) final String endTime,
            @ApiParam(value = "Size", example = "500")
            @RequestParam(value = SIZE, required = false) final String size,
            @ApiParam(value = "Page", example = "1")
            @RequestParam(value = PAGE, required = false) final String page) {
        try {
            final SearchInputParamsHolder inputParamsHolder = new SearchInputParamsHolder(userId,
                    caseRef,
                    startTime,
                    endTime,
                    size,
                    page);
            verifyRequestSearchParamsAreNotEmpty(inputParamsHolder);
            verifyRequestSearchParamsConditions(inputParamsHolder);

            log.info("Case search REPOSITORY calling getCaseSearch...");

            final CaseSearchGetResponse caseSearch = caseSearchService.getCaseSearch(inputParamsHolder);

            log.info("Case search REPOSITORY response received...");

            return new ResponseEntity<>(caseSearch, OK);
        } catch (final InvalidRequestException invalidRequestException) {
            log.error(
                    "getCaseView API call failed due to error - {}",
                    invalidRequestException.getMessage(),
                    invalidRequestException
            );
            return new ResponseEntity<>(null, BAD_REQUEST);
        }
    }
}
