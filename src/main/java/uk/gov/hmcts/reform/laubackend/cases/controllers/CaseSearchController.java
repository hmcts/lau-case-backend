package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseSearchService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsAreNotEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyRequestParamsConditions;

@RestController
@Slf4j
@Api(tags = "LAU BackEnd - API for LAU database operations.", value = "This is the Log and Audit "
        + "Back-End API that will audit case searches. "
        + "The API will be invoked by the LAU front-end service.")
public class CaseSearchController {

    @Autowired
    private CaseSearchService caseSearchService;

    @ApiOperation(
            tags = "Save case search audits", value = "Save case search audits")
    @ApiResponses({
            @ApiResponse(code = 201,
                    message = "Created SearchLog case response - includes caseSearchId from DB.",
                    response = CaseActionPostResponse.class),
            @ApiResponse(code = 400,
                    message = "Invalid case search",
                    response = CaseActionPostResponse.class),
            @ApiResponse(code = 403, message = "Forbidden",
                    response = CaseActionPostResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error",
                    response = CaseActionPostResponse.class)
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
}
