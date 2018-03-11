package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "challengeCompleted", version = "0.2")
public class ChallengeCompletedEvent implements ParticipantEvent {
    private final long timestampMillis;
    private final String participant;
    private final String challengeId;
    private final int totalClockTimeMin;
    private final int totalPenaltyTimeMin;

    @JsonCreator
    public ChallengeCompletedEvent(@JsonProperty("timestampMillis") long timestampMillis,
                                   @JsonProperty("participant") String participant,
                                   @JsonProperty("challengeId") String challengeId,
                                   @JsonProperty("totalClockTimeMin") int totalClockTimeMin,
                                   @JsonProperty("totalPenaltyTimeMin") int totalPenaltyTimeMin) {
        this.timestampMillis = timestampMillis;
        this.participant = participant;
        this.challengeId = challengeId;
        this.totalClockTimeMin = totalClockTimeMin;
        this.totalPenaltyTimeMin = totalPenaltyTimeMin;
    }
}
