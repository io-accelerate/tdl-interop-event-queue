package io.accelerate.events.interop.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.accelerate.events.interop.queue.connector.QueueEvent;

@QueueEvent(name = "languageDetected", version = "0.2")
public record ProgrammingLanguageDetectedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                              @JsonProperty("participant") String participant,
                                              @JsonProperty("challengeId") String challengeId,
                                              @JsonProperty("programmingLanguage") String programmingLanguage) implements ParticipantEvent {
}
