package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import tdl.participant.queue.connector.QueueEvent;

@Value
@QueueEvent(name = "coverageComputed", version = "0.2")
public class CoverageComputedEvent implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;
    private final String roundId;
    private final int coverage;

    public CoverageComputedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                 @JsonProperty("participant") String participant,
                                 @JsonProperty("roundId") String roundId,
                                 @JsonProperty("coverage") int coverage) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.roundId = roundId;
        this.coverage = coverage;
    }
}
