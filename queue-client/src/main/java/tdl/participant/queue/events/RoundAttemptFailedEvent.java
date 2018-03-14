package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import tdl.participant.queue.connector.QueueEvent;

@Value
@QueueEvent(name = "roundAttemptFailed", version = "0.2")
public class RoundAttemptFailedEvent implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;
    private final String roundId;
    private final int clockTimeMin;
    private final int penaltyTimeMin;

    public RoundAttemptFailedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                   @JsonProperty("participant") String participant,
                                   @JsonProperty("roundId") String roundId,
                                   @JsonProperty("clockTimeMin") int clockTimeMin,
                                   @JsonProperty("penaltyTimeMin") int penaltyTimeMin) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.roundId = roundId;
        this.clockTimeMin = clockTimeMin;
        this.penaltyTimeMin = penaltyTimeMin;
    }
}
