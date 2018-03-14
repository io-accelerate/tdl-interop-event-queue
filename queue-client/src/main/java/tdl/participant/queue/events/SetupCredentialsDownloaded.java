package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import tdl.participant.queue.connector.QueueEvent;

@Value
@QueueEvent(name = "setupCredentialsDownloaded", version = "0.2")
public class SetupCredentialsDownloaded implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;

    @JsonCreator
    public SetupCredentialsDownloaded(@JsonProperty("timestampMillis") long timestampMillis,
                                      @JsonProperty("participant") String participant) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
    }
}
