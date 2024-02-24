package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tdl.participant.queue.connector.QueueEvent;

@QueueEvent(name = "challengeStarted", version = "0.2")
public record ChallengeStartedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                     @JsonProperty("participant") String participant,
                                     @JsonProperty("challengeId") String challengeId) implements ParticipantEvent {
    @Override
    public long getTimestampMillis() {
        return timestampMillis;
    }

    @Override
    public String getParticipant() {
        return participant;
    }
}
