package uk.gov.hmcts.reform.laubackend.cases.helper;

import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public final class CaseSearchGetHelper {

    private static final String TIMESTAMP = "2021-08-23T22:20:05.023Z";

    private static List<String> caseRefs;

    private CaseSearchGetHelper() {
    }

    public static CaseSearchPostRequest getCaseSearchPostRequest(final String userId,
                                                                 final List<String> caseRefsList,
                                                                 final String timestamp) {


        final SearchLog searchLog = new SearchLog(userId == null ? "1" : userId,
                caseRefsList == null
                        ? asList("3769509556751473", "2155980079888170", "1203768774671784") : caseRefsList,
                timestamp == null ? TIMESTAMP : timestamp);

        caseRefs = searchLog.getCaseRefs();
        return new CaseSearchPostRequest(searchLog);
    }

    public static List<String> getCaseRefs(final String caseRef) {
        if (caseRefs.contains(caseRef)) {
            return caseRefs;
        }
        return new ArrayList<>();
    }
}
