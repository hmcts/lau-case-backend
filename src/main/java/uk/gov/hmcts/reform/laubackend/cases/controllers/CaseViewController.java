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
import uk.gov.hmcts.reform.laubackend.cases.dto.InputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.ViewLogPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseViewService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.CASE_JURISDICTION_ID;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.CASE_REF;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.END_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.PAGE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.SIZE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.START_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseViewConstants.USER_ID;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsAreNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsConditions;

@RestController
@Slf4j
@Api(tags = "LAU BackEnd - API for LAU database operations.", value = "This is the Log and Audit "
        + "Back-End API that will audit case view and searches. "
        + "The API will be invoked by the LAU front-end service.")

public final class CaseViewController {

    @Autowired
    private CaseViewService caseViewService;

    @ApiOperation(
            tags = "Get case audits", value = "Get list of case view audits.")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Request executed successfully. Response contains of case view logs",
                    response = CaseViewGetResponse.class),
            @ApiResponse(code = 400,
                    message =
                            "Missing userId, caseTypeId, caseJurisdictionId, "
                                    + "caseRef, startTimestamp or endTimestamp parameters.",
                    response = CaseViewGetResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = CaseViewGetResponse.class)
    })
    @GetMapping(
            path = "/audit/caseView",
            produces = APPLICATION_JSON_VALUE
    )
    @SuppressWarnings({"PMD.UseObjectForClearerAPI"})
    @ResponseBody
    public ResponseEntity<CaseViewGetResponse> getCaseView(
            @RequestParam(value = USER_ID, required = false) final String userId,
            @RequestParam(value = CASE_REF, required = false) final String caseRef,
            @RequestParam(value = CASE_TYPE_ID, required = false) final String caseTypeId,
            @RequestParam(value = CASE_JURISDICTION_ID, required = false) final String caseJurisdictionId,
            @RequestParam(value = START_TIME, required = false) final String startTime,
            @RequestParam(value = END_TIME, required = false) final String endTime,
            @RequestParam(value = SIZE, required = false) final String size,
            @RequestParam(value = PAGE, required = false) final String page) {

        try {
            final InputParamsHolder inputParamsHolder = new InputParamsHolder(userId,
                    caseRef,
                    caseTypeId,
                    caseJurisdictionId,
                    startTime,
                    endTime,
                    size,
                    page);
            verifyRequestParamsAreNotEmpty(inputParamsHolder);
            verifyRequestParamsConditions(inputParamsHolder);

            final CaseViewGetResponse caseView = caseViewService.getCaseView(inputParamsHolder);

            return new ResponseEntity<>(caseView, OK);
        } catch (final InvalidRequestException invalidRequestException) {
            log.error(
                    "getCaseView API call failed due to error - {}",
                    invalidRequestException.getMessage(),
                    invalidRequestException
            );
            return new ResponseEntity<>(null, BAD_REQUEST);
        }
    }

    @ApiOperation(
            tags = "Save case view audits", value = "Save case view audits")
    @ApiResponses({
            @ApiResponse(code = 201,
                    message = "Created viewLog case response - includes caseViewId from DB.",
                    response = CaseViewPostResponse.class),
            @ApiResponse(code = 400,
                    message = "Invalid case view",
                    response = CaseViewPostResponse.class),
            @ApiResponse(code = 403, message = "Forbidden",
                    response = CaseViewPostResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error",
                    response = CaseViewPostResponse.class)
    })
    @PostMapping(
            path = "/audit/caseView",
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<CaseViewPostResponse> saveCaseView(@RequestBody final ViewLogPostRequest viewLogPostRequest) {
        try {
            verifyRequestParamsConditions(viewLogPostRequest.getViewLog());

            final CaseViewPostResponse caseViewPostResponse = caseViewService
                    .saveCaseView(viewLogPostRequest.getViewLog());

            return new ResponseEntity<>(caseViewPostResponse, CREATED);
        } catch (final InvalidRequestException invalidRequestException) {
            log.error("saveCaseView API call failed due to error - {}",
                    invalidRequestException.getMessage(),
                    invalidRequestException
            );
            return new ResponseEntity<>(null, BAD_REQUEST);
        } catch (final Exception exception) {
            log.error("saveCaseView API call failed due to error - {}",
                    exception.getMessage(),
                    exception
            );
            return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        }
    }

}
