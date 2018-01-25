package tdl.participant.queue.connector;

public class EventSerializationException extends Exception {
    EventSerializationException(String message, Exception e) {
        super(message, e);
    }

    EventSerializationException(String message) {
        super(message);
    }
}
