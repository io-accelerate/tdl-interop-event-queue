package tdl.participant.queue.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import tdl.participant.queue.connector.QueueEvent;

@Getter
@ToString
@QueueEvent(name = "languageDetected", version = "0.1")
public class ProgrammingLanguageDetectedEvent {
    private final String participant;
    private final String challengeId;
    private final String programmingLanguage;

    public ProgrammingLanguageDetectedEvent(@JsonProperty("participant") String participant,
                                            @JsonProperty("challengeId") String challengeId,
                                            @JsonProperty("programmingLanguage") String programmingLanguage) {
        this.participant = participant;
        this.challengeId = challengeId;
        this.programmingLanguage = programmingLanguage;
    }
}
