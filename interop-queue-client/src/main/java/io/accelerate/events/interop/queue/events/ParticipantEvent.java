package io.accelerate.events.interop.queue.events;

public interface ParticipantEvent {
    long timestampMillis();
    String participant();
}
