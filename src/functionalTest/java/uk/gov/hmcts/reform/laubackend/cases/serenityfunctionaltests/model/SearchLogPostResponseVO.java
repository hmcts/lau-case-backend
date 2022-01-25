package uk.gov.hmcts.reform.laubackend.cases.serenityfunctionaltests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchLogPostResponseVO implements Serializable {

    public static final long serialVersionUID = 432973322;

    private String id;

    private String userId;

    private List<String> caseRefs;

    private String timestamp;
}
