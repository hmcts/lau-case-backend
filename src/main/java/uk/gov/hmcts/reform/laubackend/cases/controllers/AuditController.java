package uk.gov.hmcts.reform.laubackend.cases.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseViewService;
import uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsHolder;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseViewConstants.USER_ID;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseViewConstants.CASE_REF;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseViewConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseViewConstants.CASE_JURISDICTION_ID;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseViewConstants.START_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseViewConstants.END_TIME;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseViewConstants.SIZE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseViewConstants.PAGE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsAreNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsConditions;

@RestController
@Slf4j
@Api(tags = "LAU BackEnd - API for LAU database operations.", value = "This is the Log and Audit "
        + "Back-End API that will audit case view and searches. "
        + "The API will be invoked by the LAU front-end service.")

public final class AuditController {

    @Autowired
    private CaseViewService caseViewService;

    @ApiOperation(
            tags = "Get case audits", value = "Get list of case view audits.")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Request executed successfully. Response contains of case view logs",
                    response = CaseViewResponse.class),
            @ApiResponse(code = 400,
                    message =
                            "Missing userId, caseTypeId, caseJurisdictionId, "
                                    + "caseRef, startTimestamp or endTimestamp parameters.",
                    response = CaseViewResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = CaseViewResponse.class)
    })
    @GetMapping(
            path = "/audit/caseView",
            produces = APPLICATION_JSON_VALUE
    )
    @SuppressWarnings({"PMD.UseObjectForClearerAPI"})
    @ResponseBody
    public ResponseEntity<CaseViewResponse> getCaseView(
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

            final CaseViewResponse caseView = caseViewService.getCaseView(inputParamsHolder);

            return new ResponseEntity<>(caseView, OK);
        } catch (final InvalidRequestException invalidRequestException) {
            log.error(
                    "getCaseView API call failed due to error - {}",
                    invalidRequestException.getMessage(),
                    invalidRequestException
            );
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
