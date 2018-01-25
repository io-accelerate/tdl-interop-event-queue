package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "partialRoundResult", version = "0.1")
public class PartialRoundResultEvent {
    private final String participant;
    private final String roundId;
    private final int clockTimeMin;
    private final int penaltyTimeMin;

    public PartialRoundResultEvent(@JsonProperty("participant") String participant,
                               @JsonProperty("roundId") String roundId,
                               @JsonProperty("clockTimeMin") int clockTimeMin,
                               @JsonProperty("penaltyTimeMin") int penaltyTimeMin) {
        this.participant = participant;
        this.roundId = roundId;
        this.clockTimeMin = clockTimeMin;
        this.penaltyTimeMin = penaltyTimeMin;
    }
}
