package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CaseActionPostResponse {

    private ActionLogPostResponse actionLog;
}
