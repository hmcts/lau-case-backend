package uk.gov.hmcts.reform.laubackend.cases.utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

public final class CaseSearchHelper {
    private CaseSearchHelper() {
    }

    public static List<Long> convertCaseRefsToLong(final List<String> caseRefs) {
        if (!isEmpty(caseRefs)) {
            return caseRefs.stream().map(Long::parseLong).collect(toList());
        }
        return emptyList();
    }

    public static List<String> convertCaseRefsToString(final List<Long> caseRefs) {
        if (!isEmpty(caseRefs)) {
            final List<String> caseRefStringList = new ArrayList<>();
            caseRefs.forEach(caseRef -> caseRefStringList.add(Long.toString(caseRef)));
            return caseRefStringList;
        }
        return emptyList();
    }
}