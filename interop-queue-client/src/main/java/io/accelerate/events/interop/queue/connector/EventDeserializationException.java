package io.accelerate.events.interop.queue.connector;

public class EventDeserializationException extends Exception {
    public EventDeserializationException(String message) {
        super(message);
    }

    public EventDeserializationException(String message, Exception e) {
        super(message, e);
    }
}
