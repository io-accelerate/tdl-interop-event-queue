package tdl.participant.queue.connector;

@FunctionalInterface
public interface EventConsumer<T> {
    void accept(T t) throws Exception;
}
