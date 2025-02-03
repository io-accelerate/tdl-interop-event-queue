package io.accelerate.events.interop.queue.connector;

@FunctionalInterface
public interface EventInspector {
    void inspect(String eventName, String eventVersion, Object eventObject) throws Exception;
}
