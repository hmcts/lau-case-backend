package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.parse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimestampUtilTest {

    @Test
    void shouldCovertToTimestamp() {
        final TimestampUtil timestampUtil = new TimestampUtil();
        final Timestamp timestamp = valueOf(parse("2000-08-23T22:20:05"));
        final String convertedTimestamp = timestampUtil.timestampConvertor(timestamp);

        assertThat(convertedTimestamp).isEqualTo("2000-08-23T22:20:05.000Z");
    }

    @Test
    void shouldReturnTimestampFromString() {
        final TimestampUtil timestampUtil = new TimestampUtil();
        final String stringTimestamp = "2000-08-23T22:20:05.200";
        final Timestamp convertedTimestamp = timestampUtil.getTimestampValue(stringTimestamp);

        assertThat(convertedTimestamp).hasToString("2000-08-23 22:20:05.2");
    }
}
