package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "coverageComputed", version = "0.1")
public class CoverageComputedEvent {
    private final String participant;
    private final String roundId;
    private final int coverage;

    public CoverageComputedEvent(@JsonProperty("participant") String participant,
                                 @JsonProperty("roundId") String roundId,
                                 @JsonProperty("coverage") int coverage) {
        this.participant = participant;
        this.roundId = roundId;
        this.coverage = coverage;
    }
}
