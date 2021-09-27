package uk.gov.hmcts.reform.laubackend.cases.constants;

public final class RegexConstants {

    public static final String TIMESTAMP_GET_REGEX =
            "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])T(2[0-3]|[01][0-9]):?([0-5][0-9]):?([0-5][0-9])$";

    public static final String TIMESTAMP_POST_REGEX =
            "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])T(2[0-3]|[01][0-9]):?([0-5][0-9]):"
                    + "?([0-5][0-9].\\d{3})?(Z|[+-](?:2[0-3]|[01][0-9])(?::?(?:[0-5][0-9]))?)$";

    public static final String CASE_REF_REGEX = "^\\d{16}$";

    private RegexConstants() {
    }
}
