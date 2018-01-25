package tdl.participant.queue.connector;

import lombok.Value;

@Value
public class QueueSize {
    int available;
    int notVisible;
    int delayed;
}
