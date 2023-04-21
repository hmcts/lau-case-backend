package uk.gov.hmcts.reform.laubackend.cases.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;

import java.io.Serializable;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseRefsUtils.cleanUpCaseRef;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Data model for the case action log")
public class ActionLog implements Serializable {

    public static final long serialVersionUID = 432973322;

    @Schema(description = "The user on whose behalf the operation took place")
    private String userId;

    @Schema(description = "The action carried out on the case, i.e. CREATE, UPDATE, VIEW, DELETE")
    private String caseAction;

    @Schema(description = "The caseRef effected by the action")
    private String caseRef;

    @Schema(description = "The Jurisdiction Id of the case affected by the action")
    private String caseJurisdictionId;

    @Schema(description = "The Case Type Id of the case affected by the action")
    private String caseTypeId;

    @Schema(description = "When the operation took place with microseconds in iso-8601-date-and-time-format")
    private String timestamp;

    public ActionLog toDto(final CaseActionAudit caseActionAudit, final String timestamp) {
        this.userId = caseActionAudit.getUserId();
        this.caseAction = caseActionAudit.getCaseAction();
        this.caseRef = caseActionAudit.getCaseRef();
        this.caseJurisdictionId = caseActionAudit.getCaseJurisdictionId();
        this.caseTypeId = caseActionAudit.getCaseTypeId();
        this.timestamp = timestamp;

        return this;
    }

    public void setCaseRef(final String caseRef) {
        this.caseRef = isEmpty(caseRef) ? caseRef : cleanUpCaseRef(caseRef);
    }
}
