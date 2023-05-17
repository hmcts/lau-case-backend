package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Generated("jsonschema2pojo")
public class CaseActionRequestVO {

    @JsonProperty("actionLog")
    private ActionLog actionLog;

    public ActionLog getActionLog() {
        return actionLog;
    }

    public void setActionLog(ActionLog actionLog) {
        this.actionLog = actionLog;
    }

}
