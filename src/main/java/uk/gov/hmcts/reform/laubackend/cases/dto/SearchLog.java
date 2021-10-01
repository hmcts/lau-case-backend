package uk.gov.hmcts.reform.laubackend.cases.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SearchLog implements Serializable {

    public static final long serialVersionUID = 432973322;

    private String userId;
    private List<String> caseRefs;
    private String timestamp;

    public SearchLog toDto(final CaseSearchAudit caseSearchAudit, final String timestamp) {
        this.userId = caseSearchAudit.getUserId();
        this.caseRefs = caseSearchAudit.getCaseRefs();
        this.timestamp = timestamp;

        return this;
    }

}
