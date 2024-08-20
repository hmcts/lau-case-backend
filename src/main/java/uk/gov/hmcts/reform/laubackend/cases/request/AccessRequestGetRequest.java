package uk.gov.hmcts.reform.laubackend.cases.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessRequestGetRequest {

    @Parameter(name = "User ID", example = "abcd-1234-9876")
    private String userId;

    @Parameter(name = "Case Reference ID", example = "1615817621013640")
    @Pattern(regexp = RegexConstants.CASE_REF_REGEX, message = "caseRef must be 16 digits")
    private String caseRef;

    @Parameter(name = "Request Type", example = "CHALLENGED")
    private AccessRequestType requestType;

    @Parameter(name = "Start Timestamp", example = "2021-06-23T22:20:05")
    @NotBlank(message = "startTimestamp is required")
    private String startTimestamp;

    @Parameter(name = "End Timestamp", example = "2021-08-23T22:20:05")
    @NotBlank(message = "endTimestamp is required")
    private String endTimestamp;

    @Parameter(name = "Size", example = "500")
    private Integer size;

    @Parameter(name = "Page", example = "1")
    private Integer page;
}
