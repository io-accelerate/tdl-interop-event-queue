package io.accelerate.events.interop.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.accelerate.events.interop.queue.connector.QueueEvent;

@QueueEvent(name = "setupCredentialsDownloaded", version = "0.2")
public record SetupCredentialsDownloaded(@JsonProperty("timestampMillis") long timestampMillis,
                                         @JsonProperty("participant") String participant) implements ParticipantEvent {
}
