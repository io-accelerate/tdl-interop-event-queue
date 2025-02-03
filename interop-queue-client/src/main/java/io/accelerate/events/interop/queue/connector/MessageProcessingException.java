package io.accelerate.events.interop.queue.connector;

public class MessageProcessingException extends Exception {

    public MessageProcessingException(String message) {
        super(message);
    }

    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
