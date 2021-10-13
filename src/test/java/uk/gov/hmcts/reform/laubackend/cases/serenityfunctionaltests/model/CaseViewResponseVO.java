
package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "viewLog",
    "startRecordNumber",
    "moreRecords"
})
@Generated("jsonschema2pojo")
public class CaseViewResponseVO {

    @JsonProperty("viewLog")
    private List<ViewLog> viewLog = null;
    @JsonProperty("startRecordNumber")
    private Integer startRecordNumber;
    @JsonProperty("moreRecords")
    private Boolean moreRecords;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("viewLog")
    public List<ViewLog> getViewLog() {
        return viewLog;
    }

    @JsonProperty("viewLog")
    public void setViewLog(List<ViewLog> viewLog) {
        this.viewLog = viewLog;
    }

    @JsonProperty("startRecordNumber")
    public Integer getStartRecordNumber() {
        return startRecordNumber;
    }

    @JsonProperty("startRecordNumber")
    public void setStartRecordNumber(Integer startRecordNumber) {
        this.startRecordNumber = startRecordNumber;
    }

    @JsonProperty("moreRecords")
    public Boolean getMoreRecords() {
        return moreRecords;
    }

    @JsonProperty("moreRecords")
    public void setMoreRecords(Boolean moreRecords) {
        this.moreRecords = moreRecords;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}




