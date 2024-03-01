package tdl.participant.queue.events;

public interface ParticipantEvent {
    long timestampMillis();
    String participant();
}
