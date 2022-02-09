package uk.gov.hmcts.reform.laubackend.cases.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel(description = "Data model for the case search log")
public class SearchLog implements Serializable {

    public static final long serialVersionUID = 432973322;

    @ApiModelProperty(notes = "The user on whose behalf the operation took place")
    private String userId;

    @ApiModelProperty(notes = "The caseRefs effected by the search operation")
    private List<String> caseRefs;

    @ApiModelProperty(notes = "When the operation took place with microseconds in iso-8601-date-and-time-format")
    private String timestamp;

    public SearchLog toDto(final CaseSearchAudit caseSearchAuditResponse, final String timestamp) {
        this.userId = caseSearchAuditResponse.getUserId();
        this.caseRefs = Arrays.asList(caseSearchAuditResponse.getCaseRefs());
        this.timestamp = timestamp;
        return this;
    }

}
