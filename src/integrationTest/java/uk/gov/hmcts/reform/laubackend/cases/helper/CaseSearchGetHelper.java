package uk.gov.hmcts.reform.laubackend.cases.helper;

import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public final class CaseSearchGetHelper {

    private static List<Long> caseRefs;

    private CaseSearchGetHelper() {
    }

    public static CaseSearchPostRequest getCaseSearchPostRequest(final String userId,
                                                                 final List<Long> caseRefsList,
                                                                 final String timestamp) {

        final SearchLog searchLog = new SearchLog(userId == null ? "1" : userId,
                caseRefsList == null
                        ? asList(1000000000000000L, 1000000000000001L, 1000000000000002L) : caseRefsList,
                timestamp == null ? "2021-08-23T22:20:05.023Z" : timestamp);

        caseRefs = searchLog.getCaseRefs();
        return new CaseSearchPostRequest(searchLog);
    }

    public static List<Long> getCaseRefs(final String caseRef) {
        if (caseRefs.contains(Long.valueOf(caseRef))) {
            return caseRefs;
        }
        return new ArrayList<>();
    }
}
