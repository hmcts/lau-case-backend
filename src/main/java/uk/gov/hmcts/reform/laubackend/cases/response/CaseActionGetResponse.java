package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CaseActionGetResponse implements Serializable {

    public static final long serialVersionUID = 432973322;

    private List<ActionLog> actionLog;
    private int startRecordNumber;
    private boolean moreRecords;

    public static CaseActionGetResponse caseViewResponse() {
        return new CaseActionGetResponse();
    }

    public CaseActionGetResponse withActionLog(final List<ActionLog> actionLog) {
        this.actionLog = actionLog;
        return this;
    }

    public CaseActionGetResponse withStartRecordNumber(final Integer startRecordNumber) {
        this.startRecordNumber = startRecordNumber;
        return this;
    }

    public CaseActionGetResponse withMoreRecords(final boolean moreRecords) {
        this.moreRecords = moreRecords;
        return this;
    }

    public CaseActionGetResponse build() {
        final CaseActionGetResponse caseActionGetResponse = new CaseActionGetResponse();
        caseActionGetResponse.setActionLog(actionLog);
        caseActionGetResponse.setStartRecordNumber(startRecordNumber);
        caseActionGetResponse.setMoreRecords(moreRecords);
        return caseActionGetResponse;
    }
}
