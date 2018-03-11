package tdl.participant.queue.connector;

import java.util.HashMap;
import java.util.Map;

public class QueueEventHandlers {
    private EventInspector beforeEventInspector;
    private Map<String, HandleRule> handleMap;
    private EventInspector afterEventInspector;

    public QueueEventHandlers() {
        beforeEventInspector = (eventName, eventVersion, eventObject) -> {};
        handleMap = new HashMap<>();
        afterEventInspector = (eventName, eventVersion, eventObject) -> {};
    }

    public void before(EventInspector beforeEventInspector) {
        this.beforeEventInspector = beforeEventInspector;
    }

    public <T> void on(Class<T> type, EventConsumer<T> handler) throws EventProcessingException {
        QueueEvent annotation = type.getAnnotation(QueueEvent.class);
        if (annotation == null) {
            throw new EventProcessingException(type.getClass()+" not a QueueEvent");
        }
        handleMap.put(getKey(annotation.name(), annotation.version()), new HandleRule<>(type, handler));
    }

    public void after(EventInspector afterEventInspector) {
        this.afterEventInspector = afterEventInspector;
    }

    public EventInspector getBeforeEventInspector() {
        return beforeEventInspector;
    }

    public boolean canHandle(String eventName, String eventVersion) {
        String eventKey = getKey(eventName, eventVersion);
        return handleMap.containsKey(eventKey);
    }

    public HandleRule getHandleRuleFor(String eventName, String eventVersion) {
        String eventKey = getKey(eventName, eventVersion);
        return handleMap.get(eventKey);
    }

    public EventInspector getAfterEventInspector() {
        return afterEventInspector;
    }

    //~~~~

    private static String getKey(String eventName, String eventVersion) {
        return eventName+"-"+eventVersion;
    }
}
