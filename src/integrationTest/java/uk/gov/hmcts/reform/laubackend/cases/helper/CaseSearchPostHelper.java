package uk.gov.hmcts.reform.laubackend.cases.helper;

import org.apache.commons.lang3.RandomStringUtils;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public final class CaseSearchPostHelper {

    private CaseSearchPostHelper() {
    }

    public static CaseSearchPostRequest getCaseSearchPostRequest() {
        final SearchLog searchLog = new SearchLog("3748230", asList("1615817621013640",
                "1615817621013642",
                "1615817621013600",
                "1615817621013601"), "2021-08-24T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }

    public static CaseSearchPostRequest getCaseSearchPostRequest(final String usingId) {
        final SearchLog searchLog = new SearchLog(usingId, asList("1615817621013640",
                "1615817621013642",
                "1615817621013600",
                "1615817621013601"), "2021-08-24T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithValidOrInvalidCaseRefs(
            final boolean isCaseRefsAreValid) {
        final SearchLog searchLog = new SearchLog("3748230",
                isCaseRefsAreValid ? getValidCaseRefList() : getInvalidCaseRefList(),
                "2021-08-24T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithMissingCaseRefs() {
        final SearchLog searchLog = new SearchLog("3748230", emptyList(), "2021-08-25T22:20:05.023Z");

        return new CaseSearchPostRequest(searchLog);
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithMissingUserId() {
        return new CaseSearchPostRequest(new SearchLog(null,
                asList("1615817621013640",
                        "1615817621013642",
                        "1615817621013600",
                        "1615817621013601"),
                "2021-08-23T22:20:05.023Z"));
    }

    public static CaseSearchPostRequest getCaseSearchPostRequestWithInvalidUserId() {
        return new CaseSearchPostRequest(new SearchLog(
            RandomStringUtils.secure().nextAlphanumeric(65),
            List.of("1615817621013678"),
            "2021-08-26T22:20:05.023Z"));
    }

    private static List<String> getInvalidCaseRefList() {
        return asList("1615817621013888",
                "161581762_1013900  ",
                " 161581-762101_3111",
                null,
                "abv",
                "",
                "1615817621013555ABC");
    }

    private static List<String> getValidCaseRefList() {
        return asList("1615817621013888",
                "1615817621013900",
                "1615817621013111",
                "1615817621013555");
    }
}
