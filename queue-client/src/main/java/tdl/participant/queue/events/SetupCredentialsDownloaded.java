package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import tdl.participant.queue.connector.QueueEvent;

@QueueEvent(name = "setupCredentialsDownloaded", version = "0.2")
public record SetupCredentialsDownloaded(@JsonProperty("timestampMillis") long timestampMillis,
                                         @JsonProperty("participant") String participant) implements ParticipantEvent {
}
