package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("jsonschema2pojo")
public class ActionLog {

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("caseRef")
    private String caseRef;
    @JsonProperty("caseJurisdictionId")
    private String caseJurisdictionId;
    @JsonProperty("caseTypeId")
    private String caseTypeId;
    @JsonProperty("caseAction")
    private String caseAction;
    @JsonProperty("timestamp")
    private String timestamp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCaseRef() {
        return caseRef;
    }

    public void setCaseRef(String caseRef) {
        this.caseRef = caseRef;
    }

    public String getCaseJurisdictionId() {
        return caseJurisdictionId;
    }

    public void setCaseJurisdictionId(String caseJurisdictionId) {
        this.caseJurisdictionId = caseJurisdictionId;
    }

    public String getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(String caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public String getCaseAction() {
        return caseAction;
    }

    public void setCaseAction(String caseAction) {
        this.caseAction = caseAction;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
