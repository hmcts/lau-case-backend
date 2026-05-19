package uk.gov.hmcts.reform.laubackend.cases.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;
import static uk.gov.hmcts.reform.laubackend.cases.constants.RegexConstants.CASE_REF_REGEX;

@Slf4j
public final class CaseRefsUtils {

    private CaseRefsUtils() {
    }

    public static List<String> cleanUpCaseRefList(final List<String> caseRefs) {

        return caseRefs.stream()
            .filter(Objects::nonNull)
            .filter(caseRef -> !caseRef.isEmpty())
            .filter(caseRef -> !caseRef.matches("^[a-zA-Z]*$"))
            .map(caseRef -> caseRef.replaceAll("\\D+", ""))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public static String cleanUpCaseRef(final String caseRef) {

        final String cleanCaseRef = caseRef.replaceAll("\\D+", "");
        final boolean isValidCaseRef = compile(CASE_REF_REGEX).matcher(cleanCaseRef).matches();

        return isValidCaseRef ? cleanCaseRef : null;
    }
}
