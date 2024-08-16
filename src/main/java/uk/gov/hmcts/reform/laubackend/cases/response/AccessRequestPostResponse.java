package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessRequestPostResponse {
    private AccessRequestLog accessLog;
}
