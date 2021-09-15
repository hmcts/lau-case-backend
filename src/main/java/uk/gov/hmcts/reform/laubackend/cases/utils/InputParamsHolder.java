package uk.gov.hmcts.reform.laubackend.cases.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InputParamsHolder {
    private String userId;
    private String caseRef;
    private String caseTypeId;
    private String caseJurisdictionId;
    private String startTime;
    private String endTime;
    private String size;
    private String page;
}
