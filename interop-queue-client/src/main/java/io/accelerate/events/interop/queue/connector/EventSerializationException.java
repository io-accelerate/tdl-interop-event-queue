package io.accelerate.events.interop.queue.connector;

public class EventSerializationException extends Exception {
    EventSerializationException(String message, Exception e) {
        super(message, e);
    }

    EventSerializationException(String message) {
        super(message);
    }
}
