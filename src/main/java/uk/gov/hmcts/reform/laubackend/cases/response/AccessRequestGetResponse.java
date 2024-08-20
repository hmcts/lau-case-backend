package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.AccessRequestLog;

import java.util.List;

@Getter
@Setter
@Builder
public class AccessRequestGetResponse {

    private List<AccessRequestLog> accessLog;

    private int startRecordNumber;

    private boolean moreRecords;

    private long totalNumberOfRecords;
}
