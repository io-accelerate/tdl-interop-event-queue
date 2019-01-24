package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import tdl.participant.queue.connector.QueueEvent;

@Value
@QueueEvent(name = "videoRecorderStarted", version = "0.2")
public class VideoRecorderStartedEvent implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;
    private final String challengeId;
    private final String videoFileLink;

    public VideoRecorderStartedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                     @JsonProperty("participant") String participant,
                                     @JsonProperty("challengeId") String challengeId,
                                     @JsonProperty("videoFileLink") String videoFileLink) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.challengeId = challengeId;
        this.videoFileLink = videoFileLink;
    }
}
