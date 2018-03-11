package tdl.participant.queue.connector;

@FunctionalInterface
public interface EventInspector {
    void inspect(String eventName, Object eventPayload);
}
