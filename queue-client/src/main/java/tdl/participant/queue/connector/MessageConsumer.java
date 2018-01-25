package tdl.participant.queue.connector;

@FunctionalInterface
public interface MessageConsumer<T> {
    void accept(T t) throws Exception;
}
