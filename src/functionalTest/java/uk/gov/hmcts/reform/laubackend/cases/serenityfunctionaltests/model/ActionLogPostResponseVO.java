package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ActionLogPostResponseVO {

    public static final long serialVersionUID = 432973322;

    private String caseActionId;
    private String userId;
    private String caseRef;
    private String caseJurisdictionId;
    private String caseTypeId;
    private String caseAction;
    private String timestamp;
}
