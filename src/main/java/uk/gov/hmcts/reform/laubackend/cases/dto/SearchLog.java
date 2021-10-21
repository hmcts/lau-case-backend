package uk.gov.hmcts.reform.laubackend.cases.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchLog implements Serializable {

    public static final long serialVersionUID = 432973322;

    private String userId;
    private List<String> caseRefs;
    private String timestamp;


    public SearchLog toDto(final CaseSearchAudit caseSearchAuditResponse, final String timestamp) {
        this.userId = caseSearchAuditResponse.getUserId();
        this.caseRefs = getCaseRefs(caseSearchAuditResponse.getCaseSearchAuditCases());
        this.timestamp = timestamp;
        return this;
    }

    private List<String> getCaseRefs(final List<CaseSearchAuditCases> caseSearchAuditCases) {
        final List<String> caseRefs = new ArrayList<>();
        caseSearchAuditCases.forEach(caseRef -> caseRefs.add(caseRef.getCaseRef()));

        return caseRefs;
    }
}