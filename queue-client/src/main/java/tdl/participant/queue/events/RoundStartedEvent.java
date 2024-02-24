package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import tdl.participant.queue.connector.QueueEvent;

@QueueEvent(name = "roundStarted", version = "0.2")
public record RoundStartedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                @JsonProperty("participant") String participant,
                                @JsonProperty("roundId") String roundId) implements ParticipantEvent {
    @Override
    public long getTimestampMillis() {
        return timestampMillis;
    }

    @Override
    public String getParticipant() {
        return participant;
    }
}
