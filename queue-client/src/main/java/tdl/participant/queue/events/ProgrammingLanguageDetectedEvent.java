package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import tdl.participant.queue.connector.QueueEvent;

@QueueEvent(name = "languageDetected", version = "0.2")
public record ProgrammingLanguageDetectedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                              @JsonProperty("participant") String participant,
                                              @JsonProperty("challengeId") String challengeId,
                                              @JsonProperty("programmingLanguage") String programmingLanguage) implements ParticipantEvent {
}
