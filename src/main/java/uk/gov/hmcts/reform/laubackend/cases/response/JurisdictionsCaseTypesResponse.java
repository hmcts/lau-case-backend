package uk.gov.hmcts.reform.laubackend.cases.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JurisdictionsCaseTypesResponse {

    private String[] jurisdictions;
    private String[] caseTypes;
}
