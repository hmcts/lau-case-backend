
package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Generated("jsonschema2pojo")
public class CaseViewResponseVO {

    @JsonProperty("viewLog")
    private List<ViewLog> viewLog;
    @JsonProperty("startRecordNumber")
    private Integer startRecordNumber;
    @JsonProperty("moreRecords")
    private Boolean moreRecords;

    public List<ViewLog> getViewLog() {
        return viewLog;
    }

    public void setViewLog(List<ViewLog> viewLog) {
        this.viewLog = viewLog;
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




