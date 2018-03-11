package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "rawVideoUpdated", version = "0.2")
public class RawVideoUpdatedEvent implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;
    private final String challengeId;
    private final String videoLink;

    public RawVideoUpdatedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                @JsonProperty("participant") String participant,
                                @JsonProperty("challengeId") String challengeId,
                                @JsonProperty("videoLink") String videoLink) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.challengeId = challengeId;
        this.videoLink = videoLink;
    }
}
