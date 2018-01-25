package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "challengeCompleted", version = "0.1")
public class ChallengeCompletedEvent {
    private final String participant;
    private final String challengeId;
    private final int timestampSec;
    private final int totalClockTimeMin;
    private final int totalPenaltyTimeMin;

    @JsonCreator
    public ChallengeCompletedEvent(@JsonProperty("participant") String participant,
                                   @JsonProperty("challengeId") String challengeId,
                                   @JsonProperty("timestampSec") int timestampSec,
                                   @JsonProperty("totalClockTimeMin") int totalClockTimeMin,
                                   @JsonProperty("totalPenaltyTimeMin") int totalPenaltyTimeMin) {
        this.participant = participant;
        this.challengeId = challengeId;
        this.timestampSec = timestampSec;
        this.totalClockTimeMin = totalClockTimeMin;
        this.totalPenaltyTimeMin = totalPenaltyTimeMin;
    }
}
