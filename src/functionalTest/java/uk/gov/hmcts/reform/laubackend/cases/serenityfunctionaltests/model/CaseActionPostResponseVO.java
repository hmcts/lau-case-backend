package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.response.ActionLogPostResponse;

@AllArgsConstructor
@Getter
@Setter
public class CaseActionPostResponseVO {

    private ActionLogPostResponse actionLog;
}