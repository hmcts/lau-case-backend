package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CaseViewGetResponse implements Serializable {

    public static final long serialVersionUID = 432973322;

    private List<ActionLog> actionLog;
    private int startRecordNumber;
    private boolean moreRecords;

    public static CaseViewGetResponse caseViewResponse() {
        return new CaseViewGetResponse();
    }

    public CaseViewGetResponse withActionLog(final List<ActionLog> actionLog) {
        this.actionLog = actionLog;
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
        caseViewGetResponse.setActionLog(actionLog);
        caseViewGetResponse.setStartRecordNumber(startRecordNumber);
        caseViewGetResponse.setMoreRecords(moreRecords);
        return caseViewGetResponse;
    }
}
