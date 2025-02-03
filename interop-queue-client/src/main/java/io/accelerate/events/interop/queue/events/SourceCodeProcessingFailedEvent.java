package io.accelerate.events.interop.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.accelerate.events.interop.queue.connector.QueueEvent;

@QueueEvent(name = "sourceCodeProcessingFailed", version = "0.2")
public record SourceCodeProcessingFailedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                              @JsonProperty("participant") String participant,
                                              @JsonProperty("challengeId") String challengeId,
                                              @JsonProperty("errorMessage") String errorMessage) implements ProcessingFailureEvent {
}
