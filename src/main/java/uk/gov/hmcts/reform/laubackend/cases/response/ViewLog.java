package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class ViewLog implements Serializable {

    public static final long serialVersionUID = 432973322;

    private String userId;
    private String caseRef;
    private String caseJurisdictionId;
    private String caseTypeId;
    private String timestamp;

    public ViewLog toDto(final CaseViewAudit caseViewAudit) {
        this.userId = caseViewAudit.getUserId();
        this.caseRef = caseViewAudit.getCaseRef();
        this.caseJurisdictionId = caseViewAudit.getCaseJurisdictionId();
        this.caseTypeId = caseViewAudit.getCaseTypeId();
        this.timestamp = caseViewAudit.getTimestamp().toString();

        return this;
    }
}
