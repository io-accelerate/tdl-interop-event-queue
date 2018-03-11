package tdl.participant.queue.events;

public interface ParticipantEvent {
    long getTimestampMillis();
    String getParticipant();
}
