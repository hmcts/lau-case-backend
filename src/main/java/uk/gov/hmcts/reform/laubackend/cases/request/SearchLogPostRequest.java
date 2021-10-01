package uk.gov.hmcts.reform.laubackend.cases.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewLog;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class SearchLogPostRequest implements Serializable {

    public static final long serialVersionUID = 432973322;

    private SearchLog searchLog;
}
