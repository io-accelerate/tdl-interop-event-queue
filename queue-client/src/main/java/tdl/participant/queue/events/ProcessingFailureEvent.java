package tdl.participant.queue.events;

public interface ProcessingFailureEvent extends ParticipantEvent{
    String errorMessage();
}
