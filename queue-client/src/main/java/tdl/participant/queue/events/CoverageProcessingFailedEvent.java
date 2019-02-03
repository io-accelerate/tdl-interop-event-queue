package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import tdl.participant.queue.connector.QueueEvent;

@Value
@QueueEvent(name = "coverageProcessingFailed", version = "0.2")
public class CoverageProcessingFailedEvent implements ProcessingFailureEvent {
    private final long timestampMillis;
    private final String participant;
    private final String roundId;
    private final String errorMessage;


    public CoverageProcessingFailedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                         @JsonProperty("participant") String participant,
                                         @JsonProperty("roundId") String roundId,
                                         @JsonProperty("errorMessage") String errorMessage) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.roundId = roundId;
        this.errorMessage = errorMessage;
    }
}
