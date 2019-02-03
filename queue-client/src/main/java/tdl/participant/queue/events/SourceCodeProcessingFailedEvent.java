package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import tdl.participant.queue.connector.QueueEvent;

@Value
@QueueEvent(name = "sourceCodeProcessingFailed", version = "0.2")
public class SourceCodeProcessingFailedEvent implements ProcessingFailureEvent {
    private final long timestampMillis;
    private final String participant;
    private final String challengeId;
    private final String errorMessage;

    public SourceCodeProcessingFailedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                           @JsonProperty("participant") String participant,
                                           @JsonProperty("challengeId") String challengeId,
                                           @JsonProperty("errorMessage") String errorMessage) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.challengeId = challengeId;
        this.errorMessage = errorMessage;
    }
}
