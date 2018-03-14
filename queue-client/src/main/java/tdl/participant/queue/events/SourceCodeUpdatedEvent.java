package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import tdl.participant.queue.connector.QueueEvent;

@Value
@QueueEvent(name = "sourceCodeUpdated", version = "0.2")
public class SourceCodeUpdatedEvent implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;
    private final String challengeId;
    private final String sourceCodeLink;

    public SourceCodeUpdatedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                  @JsonProperty("participant") String participant,
                                  @JsonProperty("challengeId") String challengeId,
                                  @JsonProperty("sourceCodeLink") String sourceCodeLink) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.challengeId = challengeId;
        this.sourceCodeLink = sourceCodeLink;
    }
}
