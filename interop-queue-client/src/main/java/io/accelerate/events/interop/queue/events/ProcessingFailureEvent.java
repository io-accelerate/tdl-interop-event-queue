package io.accelerate.events.interop.queue.events;

public interface ProcessingFailureEvent extends ParticipantEvent{
    String errorMessage();
}
