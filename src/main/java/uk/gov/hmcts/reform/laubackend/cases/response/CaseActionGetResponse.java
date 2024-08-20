package uk.gov.hmcts.reform.laubackend.cases.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Schema(description = "Case Action GET Response")
public class CaseActionGetResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 432973322;

    private List<ActionLog> actionLog;

    @Schema(description = "The index of the first record out of the full result set provided in the result set")
    private int startRecordNumber;

    @Schema(description = "Indicates whether there are more records beyond the current page in the full result set")
    private boolean moreRecords;

    @Schema(description = "The total number of records in the full result set")
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
