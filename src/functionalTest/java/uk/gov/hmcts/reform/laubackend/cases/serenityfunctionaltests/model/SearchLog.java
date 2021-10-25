package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchLog {

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("caseRefs")
    private List<String> caseRefs;
    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("caseRef")
    public List<String> getcaseRefs() {
        return caseRefs;
    }

    @JsonProperty("caseRefs")
    public void setcaseRefs(List<String> caseRefs) {
        this.caseRefs = caseRefs;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
