package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CaseSearchPostResponse implements Serializable {

    public static final long serialVersionUID = 432973322;

    private SearchLogPostResponse searchLog;
}
