package uk.gov.hmcts.reform.laubackend.cases.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.io.Serializable;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseRefsUtils.cleanUpCaseRefList;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseSearchHelper.convertCaseRefsToString;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Schema(description = "Data model for the case search log")
public class SearchLog implements Serializable {

    public static final long serialVersionUID = 432973322;

    @Schema(description = "The user on whose behalf the operation took place")
    private String userId;

    @Schema(description = "The caseRefs effected by the search operation")
    private List<String> caseRefs;

    @Schema(description = "When the operation took place with microseconds in iso-8601-date-and-time-format")
    private String timestamp;

    public SearchLog toDto(final CaseSearchAudit caseSearchAuditResponse, final String timestamp) {
        this.userId = caseSearchAuditResponse.getUserId();
        this.caseRefs = convertCaseRefsToString(caseSearchAuditResponse.getCaseRefs());
        this.timestamp = timestamp;
        return this;
    }

    public void setCaseRefs(final List<String> caseRefs) {
        this.caseRefs = isEmpty(caseRefs) ? caseRefs : cleanUpCaseRefList(caseRefs);
    }
}
