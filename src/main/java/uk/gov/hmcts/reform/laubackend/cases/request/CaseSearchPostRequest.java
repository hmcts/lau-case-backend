package uk.gov.hmcts.reform.laubackend.cases.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CaseSearchPostRequest implements Serializable {

    public static final long serialVersionUID = 432973322;

    private SearchLog searchLog;
}
