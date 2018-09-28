package tdl.participant.queue.clitool.cmd;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

class TypeConversion {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    static Optional<Integer> asDate(String s) {
        if (s.trim().isEmpty()) {
            return Optional.empty();
        }

        TemporalAccessor temporalAccessor = DATE_FORMATTER.parse(s);
        LocalDate localDate = LocalDate.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate,LocalTime.of(0, 0), ZoneId.of("UTC"));
        Instant result = Instant.from(zonedDateTime);
        return Optional.of((int)result.getEpochSecond());
    }

    static long asMillis(Integer timestampSec) {
        long timestampAsLong = timestampSec;
        return timestampAsLong*1000;
    }

    static  Optional<String> asString(String s) {
        if (s.trim().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(s);
    }

    static  Optional<String> asUrl(String s) {
        if (!s.startsWith("http")) {
            return Optional.empty();
        }

        return Optional.of(s);
    }

    static Optional<Integer> asInt(String s) {
        if (s.trim().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(s));
    }
}
