package uk.gov.hmcts.reform.laubackend.cases.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.regex.Pattern.compile;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.CASE_REF_REGEX;

@Slf4j
public final class CaseRefsUtils {

    private CaseRefsUtils() {
    }

    public static List<String> cleanUpCaseRefList(final List<String> caseRefs) {

        caseRefs.removeIf(caseRef -> caseRef == null || caseRef.isEmpty() || caseRef.matches("^[a-zA-Z]*$"));
        caseRefs.replaceAll(s -> s.replaceAll("\\D+", ""));

        return caseRefs;
    }

    public static String cleanUpCaseRef(final String caseRef) {

        final String cleanCaseRef = caseRef.replaceAll("\\D+", "");
        final boolean isValidCaseRef = compile(CASE_REF_REGEX).matcher(cleanCaseRef).matches();

        return isValidCaseRef ? cleanCaseRef : null;
    }
}
