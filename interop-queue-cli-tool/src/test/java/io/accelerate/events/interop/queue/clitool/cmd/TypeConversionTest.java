package io.accelerate.events.interop.queue.clitool.cmd;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static io.accelerate.events.interop.queue.clitool.cmd.TypeConversion.asDate;
import static io.accelerate.events.interop.queue.clitool.cmd.TypeConversion.asMillis;

public class TypeConversionTest {

    @Test
    public void parse_date_to_epoch_seconds() {
        Integer epochSec = asDate("1971-01-01Z").orElseThrow(IllegalStateException::new);

        assertThat(epochSec, equalTo(31536000));
    }

    @Test
    public void parse_date_to_epoch_millis() {
        Integer epochSec = asDate("1971-01-01Z").orElseThrow(IllegalStateException::new);
        Long epochMillis = asMillis(epochSec);

        assertThat(epochMillis, equalTo(31536000_000L));
    }

}