
package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Generated("jsonschema2pojo")
public class CaseActionResponseVO {

    @JsonProperty("actionLog")
    private List<ActionLog> actionLog;
    @JsonProperty("startRecordNumber")
    private Integer startRecordNumber;
    @JsonProperty("moreRecords")
    private Boolean moreRecords;

    public List<ActionLog> getActionLog() {
        return actionLog;
    }

    public void setActionLog(List<ActionLog> viewLog) {
        this.actionLog = actionLog;
    }

    public Integer getStartRecordNumber() {
        return startRecordNumber;
    }

    public void setStartRecordNumber(Integer startRecordNumber) {
        this.startRecordNumber = startRecordNumber;
    }

    public Boolean getMoreRecords() {
        return moreRecords;
    }

    public void setMoreRecords(Boolean moreRecords) {
        this.moreRecords = moreRecords;
    }

}




