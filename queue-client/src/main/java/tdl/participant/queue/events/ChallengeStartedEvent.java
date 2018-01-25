package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "challengeStarted", version = "0.1")
public class ChallengeStartedEvent {
    private final String participant;
    private final String challengeId;
    private final int timestampSec;

    @JsonCreator
    public ChallengeStartedEvent(@JsonProperty("participant") String participant,
                                 @JsonProperty("challengeId") String challengeId,
                                 @JsonProperty("timestampSec") int timestampSec) {
        this.participant = participant;
        this.challengeId = challengeId;
        this.timestampSec = timestampSec;
    }
}
