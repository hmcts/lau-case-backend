package uk.gov.hmcts.reform.laubackend.cases.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CaseActionPostRequest implements Serializable {

    public static final long serialVersionUID = 432973322;

    private ActionLog actionLog;
}
