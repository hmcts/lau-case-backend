package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CaseSearchGetResponse implements Serializable {

    public static final long serialVersionUID = 432973324;

    private List<SearchLog> searchLog;
    private int startRecordNumber;
    private boolean moreRecords;

    public static CaseSearchGetResponse caseSearchResponse() {
        return new CaseSearchGetResponse();
    }

    public CaseSearchGetResponse withSearchLog(final List<SearchLog> searchLog) {
        this.searchLog = searchLog;
        return this;
    }

    public CaseSearchGetResponse withStartRecordNumber(final Integer startRecordNumber) {
        this.startRecordNumber = startRecordNumber;
        return this;
    }

    public CaseSearchGetResponse withMoreRecords(final boolean moreRecords) {
        this.moreRecords = moreRecords;
        return this;
    }

    public CaseSearchGetResponse build() {
        final CaseSearchGetResponse caseSearchGetResponse = new CaseSearchGetResponse();
        caseSearchGetResponse.setSearchLog(searchLog);
        caseSearchGetResponse.setStartRecordNumber(startRecordNumber);
        caseSearchGetResponse.setMoreRecords(moreRecords);
        return caseSearchGetResponse;
    }
}
