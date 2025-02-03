package io.accelerate.events.interop.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.accelerate.events.interop.queue.connector.QueueEvent;

@QueueEvent(name = "roundAttemptFailed", version = "0.2")
public record RoundAttemptFailedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                      @JsonProperty("participant") String participant,
                                      @JsonProperty("roundId") String roundId,
                                      @JsonProperty("clockTimeMin") int clockTimeMin,
                                      @JsonProperty("penaltyTimeMin") int penaltyTimeMin) implements ParticipantEvent {
}
