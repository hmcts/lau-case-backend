package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CaseViewResponse implements Serializable {

    public static final long serialVersionUID = 432973322;

    private List<ViewLog> viewLog;
    private int startRecordNumber;
    private boolean moreRecords;

    public static CaseViewResponse caseViewResponse() {
        return new CaseViewResponse();
    }

    public CaseViewResponse withViewLog(List<ViewLog> viewLog) {
        this.viewLog = viewLog;
        return this;
    }

    public CaseViewResponse withStartRecordNumber(Integer startRecordNumber) {
        this.startRecordNumber = startRecordNumber;
        return this;
    }

    public CaseViewResponse withMoreRecords(boolean moreRecords) {
        this.moreRecords = moreRecords;
        return this;
    }

    public CaseViewResponse build() {
        CaseViewResponse caseViewResponse = new CaseViewResponse();
        caseViewResponse.setViewLog(viewLog);
        caseViewResponse.setStartRecordNumber(startRecordNumber);
        caseViewResponse.setMoreRecords(moreRecords);
        return caseViewResponse;
    }
}
