package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import tdl.participant.queue.connector.QueueEvent;

@Value
@QueueEvent(name = "roundStarted", version = "0.2")
public class RoundStartedEvent implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;
    private final String roundId;

    @JsonCreator
    public RoundStartedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                             @JsonProperty("participant") String participant,
                             @JsonProperty("roundId") String roundId) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.roundId = roundId;
    }
}
