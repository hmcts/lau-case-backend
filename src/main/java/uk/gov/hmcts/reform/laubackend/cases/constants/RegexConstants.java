package uk.gov.hmcts.reform.laubackend.cases.constants;

public final class RegexConstants {

    public static final String TIMESTAMP_GET_REGEX =
            "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])T(2[0-3]|[01][0-9]):?([0-5][0-9]):?([0-5][0-9])$";

    /*
    Part after the letter T
    (2[0-3]|[01][0-9]) - hours (00-23)
    :? - optional colon
    ([0-5][0-9]) - minutes (00-59)
    :? - optional colon
    ([0-5][0-9].\\d+)? - optional seconds (00-59), if provided, must be followed by a dot and at least one digit
    (Z|[+-](?:2[0-3]|[01][0-9])(?::?(?:[0-5][0-9]))?) - timezone, either Z or +HH:MM or -HH:MM
        Z - literal Z
        | - or
        [+-] - plus or minus sign
        (?:2[0-3]|[01][0-9]) - hours (00-23)
        (?::?(?:[0-5][0-9]))? - optional colon and minutes (00-59)

    This regex allows for the following formats, that most fail the conversion to timestamp:
    - 2024-08-15T1345Z - seconds are optional
    - 2024-08-15T13:45Z - colon is optional
    - 2024-08-15T2245:Z - colon can be provided even if seconds are not
    + 2024-08-15T22:45:32.123Z - if seconds given, they must have a fraction part with at least single digit
    - 2024-08-15T22:45:12M32Z - seconds fractional part can be separated by any single character
    - 2024-08-15T22:45:12.32+01
    - 2024-08-15T22:45:12.32+0112
    + 2024-08-15T22:45:12.32+01:12
    Only those marked with + can be converted to timestamp without exception
     */
    public static final String TIMESTAMP_POST_REGEX =
            "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])T(2[0-3]|[01][0-9]):?([0-5][0-9]):"
                    + "?([0-5][0-9].\\d+)?(Z|[+-](?:2[0-3]|[01][0-9])(?::?(?:[0-5][0-9]))?)$";

    public static final String CASE_REF_REGEX = "^\\d{16}$";

    private RegexConstants() {
    }
}
