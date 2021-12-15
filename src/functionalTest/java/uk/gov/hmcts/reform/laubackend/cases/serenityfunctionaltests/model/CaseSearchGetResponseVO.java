package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CaseSearchGetResponseVO {

    @JsonProperty("searchLog")
    private List<SearchLog> searchLog;

    @JsonProperty("startRecordNumber")
    private Integer startRecordNumber;

    @JsonProperty("moreRecords")
    private Boolean moreRecords;

    @JsonProperty("totalNumberOfRecords")
    private Long totalNumberOfRecords;

    @JsonProperty("searchLog")
    public List<SearchLog> getSearchLog() {
        return searchLog;
    }

    @JsonProperty("totalNumberOfRecords")
    public Long getTotalNumberOfRecords() {
        return totalNumberOfRecords;
    }

    @JsonProperty("totalNumberOfRecords")
    public void setTotalNumberOfRecords(Long totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }

    @JsonProperty("searchLog")
    public void setSearchLog(List<SearchLog> searchLog) {
        this.searchLog = searchLog;
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



}
