package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "sourceCodeUpdated", version = "0.1")
public class SourceCodeUpdatedEvent {
    private final String participant;
    private final String challengeId;
    private final String sourceCodeLink;

    public SourceCodeUpdatedEvent(@JsonProperty("participant") String participant,
                                  @JsonProperty("challengeId") String challengeId,
                                  @JsonProperty("sourceCodeLink") String sourceCodeLink) {
        this.participant = participant;
        this.challengeId = challengeId;
        this.sourceCodeLink = sourceCodeLink;
    }
}
