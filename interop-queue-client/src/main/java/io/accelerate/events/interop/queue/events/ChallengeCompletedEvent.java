package io.accelerate.events.interop.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.accelerate.events.interop.queue.connector.QueueEvent;

@QueueEvent(name = "challengeCompleted", version = "0.2")
public record ChallengeCompletedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                      @JsonProperty("participant") String participant,
                                      @JsonProperty("challengeId") String challengeId,
                                      @JsonProperty("totalClockTimeMin") int totalClockTimeMin,
                                      @JsonProperty("totalPenaltyTimeMin") int totalPenaltyTimeMin) implements ParticipantEvent {
}
