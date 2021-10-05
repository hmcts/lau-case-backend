package uk.gov.hmcts.reform.laubackend.cases.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActionLog implements Serializable {

    public static final long serialVersionUID = 432973322;

    private String userId;
    private String action;
    private String caseRef;
    private String caseJurisdictionId;
    private String caseTypeId;
    private String timestamp;

    public ActionLog toDto(final CaseActionAudit caseActionAudit, final String timestamp) {
        this.userId = caseActionAudit.getUserId();
        this.action = caseActionAudit.getAction();
        this.caseRef = caseActionAudit.getCaseRef();
        this.caseJurisdictionId = caseActionAudit.getCaseJurisdictionId();
        this.caseTypeId = caseActionAudit.getCaseTypeId();
        this.timestamp = timestamp;

        return this;
    }

}
