package uk.gov.hmcts.reform.laubackend.cases.utils;

import java.util.List;

public final class CaseRefsUtils {

    public static List<String> cleanUpCaseRefList(final List<String> caseRefs) {
        caseRefs.removeIf(caseRef -> caseRef == null || caseRef.equals("") || caseRef.equals("null"));
        caseRefs.replaceAll(s -> s.replaceAll("\\D+",""));

        return caseRefs;
    }

    public static String cleanUpCaseRef(final String caseRef) {
        return caseRef.replaceAll("\\D+","");
    }

    private CaseRefsUtils() {
    }
}
