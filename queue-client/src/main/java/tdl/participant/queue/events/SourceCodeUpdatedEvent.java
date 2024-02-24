package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import tdl.participant.queue.connector.QueueEvent;

@QueueEvent(name = "sourceCodeUpdated", version = "0.2")
public record SourceCodeUpdatedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                     @JsonProperty("participant") String participant,
                                     @JsonProperty("challengeId") String challengeId,
                                     @JsonProperty("sourceCodeLink") String sourceCodeLink) implements ParticipantEvent {
    @Override
    public long getTimestampMillis() {
        return timestampMillis;
    }

    @Override
    public String getParticipant() {
        return participant;
    }
}
