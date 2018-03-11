package tdl.participant.queue.connector;

public class HandleRule<T> {
    private final Class<T> type;
    private final EventConsumer<T> consumer;

    HandleRule(Class<T> type, EventConsumer<T> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    public Class<T> getType() {
        return type;
    }

    public EventConsumer<T> getConsumer() {
        return consumer;
    }
}
