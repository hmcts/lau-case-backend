package uk.gov.hmcts.reform.laubackend.cases.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessRequestPostRequest {

    @Valid
    @NotNull(message = "accessLog is required")
    private AccessRequestLog accessLog;
}
