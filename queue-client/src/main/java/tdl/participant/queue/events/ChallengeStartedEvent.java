package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "challengeStarted", version = "0.2")
public class ChallengeStartedEvent implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;
    private final String challengeId;

    @JsonCreator
    public ChallengeStartedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                 @JsonProperty("participant") String participant,
                                 @JsonProperty("challengeId") String challengeId) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.challengeId = challengeId;
    }
}
