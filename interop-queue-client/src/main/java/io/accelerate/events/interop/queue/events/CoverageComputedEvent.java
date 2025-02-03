package io.accelerate.events.interop.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.accelerate.events.interop.queue.connector.QueueEvent;

@QueueEvent(name = "coverageComputed", version = "0.2")
public record CoverageComputedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                    @JsonProperty("participant") String participant,
                                    @JsonProperty("roundId") String roundId,
                                    @JsonProperty("coverage") int coverage) implements ParticipantEvent {
}
