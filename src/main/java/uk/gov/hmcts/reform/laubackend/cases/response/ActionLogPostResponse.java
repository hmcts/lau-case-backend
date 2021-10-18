package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

@NoArgsConstructor
@Getter
@Setter
public class ActionLogPostResponse {

    public static final long serialVersionUID = 432973322;

    private String caseActionId;
    private String userId;
    private String caseRef;
    private String caseJurisdictionId;
    private String caseTypeId;
    private String caseAction;
    private String timestamp;

    public ActionLogPostResponse toDto(final CaseActionAudit caseActionAudit, final String timestamp) {
        this.caseActionId = caseActionAudit.getCaseActionId().toString();
        this.userId = caseActionAudit.getUserId();
        this.caseRef = caseActionAudit.getCaseRef();
        this.caseJurisdictionId = caseActionAudit.getCaseJurisdictionId();
        this.caseAction = caseActionAudit.getCaseAction();
        this.caseTypeId = caseActionAudit.getCaseTypeId();
        this.timestamp = timestamp;

        return this;
    }
}
