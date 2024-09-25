package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestGetRequest;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.AccessRequestGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.AccessRequestPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.AccessRequestService;

import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsVerifier.verifyAccessRequestGetTimestamp;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyRequestAccessRequestParamsPresence;


@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Challenged and Specific access request endpoints")
public class AccessRequestController {

    private final AccessRequestService accessRequestService;

    @Operation(
        tags = "POST end-points", summary = "Save access request",
        description = "This will save specific or challenged access request"
    )
    @ApiResponse(responseCode = "201", description = "Access request saved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request received")
    @ApiResponse(responseCode = "403", description = "Authorization failed")
    @ApiResponse(responseCode = "500", description = "Error occurred while saving access request")
    @PostMapping("/audit/accessRequest")
    public ResponseEntity<AccessRequestPostResponse> saveAccessRequest(
        @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
        @RequestHeader(value = CommonConstants.SERVICE_AUTHORISATION_HEADER, required = true)
        final String serviceAuthorization,
        @Valid @RequestBody final AccessRequestPostRequest accessRequestPostRequest
    ) {
        try {
            AccessRequestLog accessRequestLog = accessRequestPostRequest.getAccessLog();
            AccessRequestLog savedAccessRequestLog = accessRequestService.save(accessRequestLog);
            AccessRequestPostResponse postResponse = new AccessRequestPostResponse(savedAccessRequestLog);
            return new ResponseEntity<>(postResponse, CREATED);
        } catch (DateTimeParseException dpe) {
            log.error("Invalid request received - {}", dpe.getMessage());
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error occurred while saving access request - {}", e.getMessage(), e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
        tags = "GET end-points", summary = "Retrieve access request records",
        description = "Endpoint to retrieve access request records based on search conditions"
    )
    @ApiResponse(responseCode = "200", description = "A list of Access Request records")
    @ApiResponse(responseCode = "400", description = "Missing userId, caseRef, request type, start or end timestamp")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping(path = "/audit/accessRequest")
    public ResponseEntity<AccessRequestGetResponse> getAccessRequest(
        @Parameter(name = "Authorization", example = "Bearer eyJ0eXAiOiJK.........")
        @RequestHeader(value = AUTHORISATION_HEADER) String authToken,
        @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
        @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String serviceAuthToken,
        @ParameterObject AccessRequestGetRequest requestQuery
    ) {
        try {
            verifyRequestAccessRequestParamsPresence(requestQuery);
            verifyAccessRequestGetTimestamp(requestQuery.getStartTimestamp());
            verifyAccessRequestGetTimestamp(requestQuery.getEndTimestamp());
            AccessRequestGetResponse response = accessRequestService.getAccessRequestRecords(requestQuery);
            return ResponseEntity.ok(response);
        } catch (InvalidRequestException ire) {
            log.error("Invalid request received - {}", ire.getMessage());
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
