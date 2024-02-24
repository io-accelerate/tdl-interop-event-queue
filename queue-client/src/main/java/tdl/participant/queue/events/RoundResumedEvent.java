package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tdl.participant.queue.connector.QueueEvent;

@QueueEvent(name = "roundResumed", version = "0.2")
public record RoundResumedEvent(@JsonProperty("timestampMillis") long timestampMillis,
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
