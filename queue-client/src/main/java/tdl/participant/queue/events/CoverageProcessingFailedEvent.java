package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import tdl.participant.queue.connector.QueueEvent;

@QueueEvent(name = "coverageProcessingFailed", version = "0.2")
public record CoverageProcessingFailedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                            @JsonProperty("participant") String participant,
                                            @JsonProperty("roundId") String roundId,
                                            @JsonProperty("errorMessage") String errorMessage) implements ProcessingFailureEvent {
    @Override
    public long getTimestampMillis() {
        return timestampMillis;
    }

    @Override
    public String getParticipant() {
        return participant;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
