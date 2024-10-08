package uk.gov.hmcts.reform.laubackend.cases.utils;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.parse;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@SuppressWarnings("PMD.SimpleDateFormatNeedsLocale")
@Service
public class TimestampUtil {

    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public String timestampConvertor(final Timestamp timestamp) {
        if (timestamp != null) {
            return new SimpleDateFormat(TIMESTAMP_PATTERN).format(timestamp);
        }
        return null;
    }

    public Timestamp getUtcTimestampValue(final String timestamp) {
        if (!isEmpty(timestamp)) {
            final LocalDateTime localDateTime = Instant.parse(timestamp)
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();

            return valueOf(localDateTime);
        }
        return null;
    }

    public Timestamp getTimestampValue(final String timestamp) {
        if (!isEmpty(timestamp)) {
            return valueOf(parse(timestamp));
        }
        return null;
    }
}
