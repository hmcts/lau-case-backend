package uk.gov.hmcts.reform.laubackend.cases.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.regex.Pattern.compile;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.CASE_REF_REGEX;

@SuppressWarnings({"PMD.ConfusingTernary"})
@Slf4j
public final class CaseRefsUtils {

    private CaseRefsUtils() {
    }

    public static List<String> cleanUpCaseRefList(final List<String> caseRefs) {
        log.info("Incoming caseRef list for saveCaseSearch: {}", caseRefs);

        caseRefs.removeIf(caseRef -> caseRef == null || caseRef.equals("") || caseRef.matches("^[a-zA-Z]*$"));
        caseRefs.replaceAll(s -> s.replaceAll("\\D+", ""));

        log.info("CaseRef list after clean up for saveCaseSearch: {}", caseRefs);

        return caseRefs;
    }

    public static String cleanUpCaseRef(final String caseRef) {
        log.info("Incoming caseRef for saveCaseAction: {}", caseRef);

        final String cleanCaseRef = caseRef.replaceAll("\\D+", "");
        final boolean isValidCaseRef = compile(CASE_REF_REGEX).matcher(cleanCaseRef).matches();

        log.info("CaseRef after clean up for saveCaseAction: {}", cleanCaseRef);

        return isValidCaseRef ? cleanCaseRef : null;
    }
}
