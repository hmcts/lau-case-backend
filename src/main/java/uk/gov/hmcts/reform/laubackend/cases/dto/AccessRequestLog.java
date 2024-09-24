package uk.gov.hmcts.reform.laubackend.cases.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestAction;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;
import uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants;
import uk.gov.hmcts.reform.laubackend.cases.domain.AccessRequest;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestLog {

    @NotNull(message = "requestType is required")
    private AccessRequestType requestType;

    @NotBlank(message = "userId is required")
    @Size(min = 1, max = 64, message = "userId length must be less than or equal to 64")
    private String userId;

    @NotBlank(message = "caseRef is required")
    @Pattern(regexp = RegexConstants.CASE_REF_REGEX, message = "caseRef must be 16 digits")
    private String caseRef;

    @NotBlank(message = "reason is required")
    private String reason;

    @NotNull(message = "action is required")
    private AccessRequestAction action;

    @JsonProperty("requestStartTimestamp")
    private String requestStart;

    @JsonProperty("requestEndTimestamp")
    private String requestEnd;

    @NotBlank(message = "timestamp is required")
    private String timestamp;

    public AccessRequest toModel() {
        final TimestampUtil timestampUtil = new TimestampUtil();

        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setRequestType(this.getRequestType().name());
        accessRequest.setUserId(this.getUserId());
        accessRequest.setCaseRef(this.getCaseRef());
        accessRequest.setReason(this.getReason());
        accessRequest.setAction(this.getAction().name());
        accessRequest.setRequestStart(timestampUtil.getUtcTimestampValue(this.getRequestStart()));
        accessRequest.setRequestEnd(timestampUtil.getUtcTimestampValue(this.getRequestEnd()));
        accessRequest.setTimestamp(timestampUtil.getUtcTimestampValue(this.getTimestamp()));

        return accessRequest;
    }

    public static AccessRequestLog modelToDto(AccessRequest accessRequest) {
        TimestampUtil timestampUtil = new TimestampUtil();

        AccessRequestLogBuilder builder = AccessRequestLog.builder()
            .requestType(AccessRequestType.valueOf(accessRequest.getRequestType()))
            .userId(accessRequest.getUserId())
            .caseRef(accessRequest.getCaseRef())
            .reason(accessRequest.getReason())
            .action(AccessRequestAction.valueOf(accessRequest.getAction()))
            .requestStart(timestampUtil.timestampConvertor(accessRequest.getRequestStart()))
            .requestEnd(timestampUtil.timestampConvertor(accessRequest.getRequestEnd()))
            .timestamp(timestampUtil.timestampConvertor(accessRequest.getTimestamp()));

        return builder.build();

    }
}
