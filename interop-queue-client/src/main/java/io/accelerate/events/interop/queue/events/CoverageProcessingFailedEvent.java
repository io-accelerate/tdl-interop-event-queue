package io.accelerate.events.interop.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.accelerate.events.interop.queue.connector.QueueEvent;

@QueueEvent(name = "coverageProcessingFailed", version = "0.2")
public record CoverageProcessingFailedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                            @JsonProperty("participant") String participant,
                                            @JsonProperty("roundId") String roundId,
                                            @JsonProperty("errorMessage") String errorMessage) implements ProcessingFailureEvent {
}
