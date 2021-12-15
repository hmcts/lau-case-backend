package uk.gov.hmcts.reform.laubackend.cases.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel(description = "Case Action GET Response")
public class CaseActionGetResponse implements Serializable {

    public static final long serialVersionUID = 432973322;

    private List<ActionLog> actionLog;

    @ApiModelProperty(notes = "The index of the first record out of the full result set provided in the result set")
    private int startRecordNumber;

    @ApiModelProperty(notes = "Indicates whether there are more records beyond the current page in the full result set")
    private boolean moreRecords;

    @ApiModelProperty(notes = "The total number of records in the full result set")
    private long totalNumberOfRecords;

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

    public CaseActionGetResponse withTotalNumberOfRecords(final long totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
        return this;
    }

    public CaseActionGetResponse build() {
        final CaseActionGetResponse caseActionGetResponse = new CaseActionGetResponse();
        caseActionGetResponse.setActionLog(actionLog);
        caseActionGetResponse.setStartRecordNumber(startRecordNumber);
        caseActionGetResponse.setMoreRecords(moreRecords);
        caseActionGetResponse.setTotalNumberOfRecords(totalNumberOfRecords);
        return caseActionGetResponse;
    }
}
