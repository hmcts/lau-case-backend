package uk.gov.hmcts.reform.laubackend.cases.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActionInputParamsHolder {
    private String userId;
    private String caseRef;
    private String caseTypeId;
    private String caseAction;
    private String caseJurisdictionId;
    private String startTime;
    private String endTime;
    private String size;
    private String page;
}
