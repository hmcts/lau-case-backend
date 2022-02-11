package uk.gov.hmcts.reform.laubackend.cases.utils;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

public final class CaseSearchHelper {
    private CaseSearchHelper() {
    }

    public static List<Long> convertCaseRefsToLong(final List<String> caseRefs) {
        if (!isEmpty(caseRefs)) {
            return caseRefs.stream().map(Long::parseLong).collect(toList());
        }
        return null;
    }

    public static List<String> convertCaseRefsToString(final List<Long> caseRefs) {
        if (!isEmpty(caseRefs)) {
            return caseRefs.stream().map(String::valueOf).collect(toList());
        }
        return null;
    }
}