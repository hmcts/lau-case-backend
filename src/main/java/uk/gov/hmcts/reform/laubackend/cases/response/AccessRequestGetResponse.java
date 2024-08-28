package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestGetResponse {

    private List<AccessRequestLog> accessLog;

    private int startRecordNumber;

    private boolean moreRecords;

    private long totalNumberOfRecords;
}
