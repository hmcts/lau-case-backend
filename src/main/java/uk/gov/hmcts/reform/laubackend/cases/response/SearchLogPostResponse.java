package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SearchLogPostResponse {

    public static final long serialVersionUID = 432973322;

    private String userId;
    private List<String> caseRefs;
    private String timestamp;

    public SearchLogPostResponse toDto(final CaseSearchAudit caseSearchAudit, final String timestamp) {
        this.userId = caseSearchAudit.getUserId();
        this.caseRefs = caseSearchAudit.getCaseRefs();
        this.timestamp = timestamp;

        return this;
    }
}
