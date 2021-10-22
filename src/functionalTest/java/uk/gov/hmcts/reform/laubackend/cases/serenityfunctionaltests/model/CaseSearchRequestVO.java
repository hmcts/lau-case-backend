package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaseSearchRequestVO {

    @JsonProperty("searchLog")
    private SearchLog searchLog;

    @JsonProperty("searchLog")
    public SearchLog getSearchLog() {
        return searchLog;
    }

    @JsonProperty("searchLog")
    public void setSearchLog(SearchLog searchLog) {
        this.searchLog = searchLog;
    }

}
