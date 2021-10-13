package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Generated("jsonschema2pojo")
public class CaseViewRequestVO {

    @JsonProperty("viewLog")
    private ViewLog viewLog;

    public ViewLog getViewLog() {
        return viewLog;
    }

    public void setViewLog(ViewLog viewLog) {
        this.viewLog = viewLog;
    }

}
