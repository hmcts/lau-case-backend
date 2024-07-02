package uk.gov.hmcts.reform.laubackend.cases.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidRequestException;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseActionService;
import uk.gov.hmcts.reform.laubackend.cases.service.CaseSearchService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.cases.utils.NotEmptyInputParamsVerifier.verifyIdNotEmpty;

@Hidden
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Lau case database delete operations.", description = "This is the Log and Audit "
        + "Back-End API that will delete database records to facilitate removal of the test data,"
        + " generated by functional and e2e tests")
@SuppressWarnings({"PMD.ExcessiveImports"})
public class DeleteController {

    private final CaseSearchService caseSearchService;

    private final CaseActionService caseActionService;

    @Operation(
            tags = "DELETE end-points", summary = "Delete case search record from the database.",
            description = "This API will delete a record from the lau database for the given case search id. "
                    + "It is intended to be called from the test api for testing purposes."
    )
    @ApiResponse(responseCode = "200", description = "Case search record has been deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Case search id not found in the database")
    @ApiResponse(responseCode = "400", description = "Missing case search id from the API request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @DeleteMapping(
        path = "/audit/caseSearch/deleteCaseSearchRecord",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteCaseSearchRecordById(@RequestParam("caseSearchId") final String caseSearchId) {
        try {
            verifyIdNotEmpty(caseSearchId);
            // as of Spring Data 2022.0 release
            // deleteById(ID id) should not throw an exception when no entity gets deleted.
            // therefore we manually check if case search exists and throw an exception if not
            caseSearchService.verifyCaseSearchExists(caseSearchId);
            caseSearchService.deleteCaseSearchById(caseSearchId);

            return new ResponseEntity<>(OK);
        } catch (final InvalidRequestException invalidRequestException) {
            return getExceptionResponseEntity(invalidRequestException, BAD_REQUEST);
        } catch (final EmptyResultDataAccessException emptyResultDataAccessException) {
            return getExceptionResponseEntity(emptyResultDataAccessException, NOT_FOUND);
        } catch (final Exception exception) {
            return getExceptionResponseEntity(exception, INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(
        tags = "DELETE end-points", summary = "Delete case action record from the database.",
        description = "This API will delete a record from the lau database for the given case action id. "
            + "It is intended to be called from the test api for testing purposes."
    )
    @ApiResponse(responseCode = "200", description = "Case action record has been deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Case action id not found in the database")
    @ApiResponse(responseCode = "400", description = "Missing case search id from the API request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @DeleteMapping(
        path = "/audit/caseAction/deleteCaseActionRecord",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteCaseActionRecordById(@RequestParam("caseActionId") final String caseActionId) {
        try {
            verifyIdNotEmpty(caseActionId);
            // as of Spring Data 2022.0 release
            // deleteById(ID id) should not throw an exception when no entity gets deleted.
            // therefore we manually check if case action exists and throw an exception if not
            caseActionService.verifyCaseActionExists(caseActionId);
            caseActionService.deleteCaseActionById(caseActionId);

            return new ResponseEntity<>(OK);
        } catch (final InvalidRequestException invalidRequestException) {
            return getExceptionResponseEntity(invalidRequestException, BAD_REQUEST);
        } catch (final EmptyResultDataAccessException emptyResultDataAccessException) {
            return getExceptionResponseEntity(emptyResultDataAccessException, NOT_FOUND);
        } catch (final Exception exception) {
            return getExceptionResponseEntity(exception, INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> getExceptionResponseEntity(final Exception exception, final HttpStatus httpStatus) {
        log.error(
                "deleteRecordById API call failed due to error - {}",
                exception.getMessage(),
                exception
        );
        return new ResponseEntity<>(httpStatus);
    }
}
