package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;
import uk.gov.hmcts.reform.laubackend.cases.request.AccessRequestPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.AccessRequestPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.service.AccessRequestService;

import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


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
            AccessRequestLog accessRequestLog = accessRequestPostRequest.getAccessRequestLog();
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
}
