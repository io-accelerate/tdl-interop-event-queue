package tdl.participant.queue.connector;

@FunctionalInterface
public interface EventInspector {
    void inspect(String eventName, String eventVersion, Object eventObject);
}
