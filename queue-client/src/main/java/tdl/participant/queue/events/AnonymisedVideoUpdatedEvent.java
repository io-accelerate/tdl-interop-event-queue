package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import tdl.participant.queue.connector.QueueEvent;

@QueueEvent(name = "anonymisedVideoUpdated", version = "0.2")
public record AnonymisedVideoUpdatedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                          @JsonProperty("participant") String participant,
                                          @JsonProperty("challengeId") String challengeId,
                                          @JsonProperty("videoLink") String videoLink) implements ParticipantEvent {
}
