package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "userId",
    "caseRef",
    "caseJurisdictionId",
    "caseTypeId",
    "timestamp"
})
@Generated("jsonschema2pojo")
public class ViewLog {

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("caseRef")
    private String caseRef;
    @JsonProperty("caseJurisdictionId")
    private String caseJurisdictionId;
    @JsonProperty("caseTypeId")
    private String caseTypeId;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("caseRef")
    public String getCaseRef() {
        return caseRef;
    }

    @JsonProperty("caseRef")
    public void setCaseRef(String caseRef) {
        this.caseRef = caseRef;
    }

    @JsonProperty("caseJurisdictionId")
    public String getCaseJurisdictionId() {
        return caseJurisdictionId;
    }

    @JsonProperty("caseJurisdictionId")
    public void setCaseJurisdictionId(String caseJurisdictionId) {
        this.caseJurisdictionId = caseJurisdictionId;
    }

    @JsonProperty("caseTypeId")
    public String getCaseTypeId() {
        return caseTypeId;
    }

    @JsonProperty("caseTypeId")
    public void setCaseTypeId(String caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
