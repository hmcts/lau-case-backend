package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseSearchService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.CASE_REF;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.END_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.PAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.SIZE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.START_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.USER_ID;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsAreNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsConditions;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestSearchParamsAreNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestSearchParamsConditions;

@RestController
@Slf4j
@Api(tags = "LAU BackEnd - API for LAU database operations.", value = "This is the Log and Audit "
        + "Back-End API that will audit case searches. "
        + "The API will be invoked by the LAU front-end service.")
@SuppressWarnings("PMD.ExcessiveImports")
public class CaseSearchController {

    @Autowired
    private CaseSearchService caseSearchService;

    @ApiOperation(
            tags = "Save case search audits", value = "Save case search audits")
    @ApiResponses({
            @ApiResponse(code = 201,
                    message = "Created SearchLog case response - includes caseSearchId from DB.",
                    response = CaseViewPostResponse.class),
            @ApiResponse(code = 400,
                    message = "Invalid case search",
                    response = CaseViewPostResponse.class),
            @ApiResponse(code = 403, message = "Forbidden",
                    response = CaseViewPostResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error",
                    response = CaseViewPostResponse.class)
    })
    @PostMapping(
            path = "/audit/caseSearch",
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<CaseSearchPostRequest> saveCaseSearch(@RequestBody final CaseSearchPostRequest
                                                                        caseSearchPostRequest) {
        try {
            verifyRequestParamsAreNotEmpty(caseSearchPostRequest);
            verifyRequestParamsConditions(caseSearchPostRequest.getSearchLog());

            final CaseSearchPostRequest caseSearchAudit = caseSearchService.saveCaseSearch(caseSearchPostRequest);

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

    @ApiOperation(
        tags = "Get case search audits", value = "Get case search audits")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "Request executed successfully. Response contains of case search logs",
            response = CaseSearchGetResponse.class),
        @ApiResponse(code = 400,
            message =
                "Missing userId, caseRef, startTimestamp or endTimestamp parameters.",
            response = CaseSearchGetResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = CaseViewGetResponse.class)
    })
    @GetMapping(
        path = "/audit/caseSearch",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
    )
    @SuppressWarnings({"PMD.UseObjectForClearerAPI"})
    @ResponseBody
    public ResponseEntity<CaseSearchGetResponse> getCaseSearch(
        @RequestParam(value = USER_ID, required = false) final String userId,
        @RequestParam(value = CASE_REF, required = false) final String caseRef,
        @RequestParam(value = START_TIME, required = false) final String startTime,
        @RequestParam(value = END_TIME, required = false) final String endTime,
        @RequestParam(value = SIZE, required = false) final String size,
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

            final CaseSearchGetResponse caseSearch = caseSearchService.getCaseSearch(inputParamsHolder);

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
