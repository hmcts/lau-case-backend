package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewLog;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CaseViewGetResponse implements Serializable {

    public static final long serialVersionUID = 432973322;

    private List<ViewLog> viewLog;
    private int startRecordNumber;
    private boolean moreRecords;

    public static CaseViewGetResponse caseViewResponse() {
        return new CaseViewGetResponse();
    }

    public CaseViewGetResponse withViewLog(final List<ViewLog> viewLog) {
        this.viewLog = viewLog;
        return this;
    }

    public CaseViewGetResponse withStartRecordNumber(final Integer startRecordNumber) {
        this.startRecordNumber = startRecordNumber;
        return this;
    }

    public CaseViewGetResponse withMoreRecords(final boolean moreRecords) {
        this.moreRecords = moreRecords;
        return this;
    }

    public CaseViewGetResponse build() {
        final CaseViewGetResponse caseViewGetResponse = new CaseViewGetResponse();
        caseViewGetResponse.setViewLog(viewLog);
        caseViewGetResponse.setStartRecordNumber(startRecordNumber);
        caseViewGetResponse.setMoreRecords(moreRecords);
        return caseViewGetResponse;
    }
}
