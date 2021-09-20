package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;

@NoArgsConstructor
@Getter
@Setter
public class ViewLogPostResponse {

    public static final long serialVersionUID = 432973322;

    private String caseViewId;
    private String userId;
    private String caseRef;
    private String caseJurisdictionId;
    private String caseTypeId;
    private String timestamp;

    public ViewLogPostResponse toDto(final CaseViewAudit caseViewAudit, final String timestamp) {
        this.caseViewId = caseViewAudit.getCaseViewId().toString();
        this.userId = caseViewAudit.getUserId();
        this.caseRef = caseViewAudit.getCaseRef();
        this.caseJurisdictionId = caseViewAudit.getCaseJurisdictionId();
        this.caseTypeId = caseViewAudit.getCaseTypeId();
        this.timestamp = timestamp;

        return this;
    }
}
