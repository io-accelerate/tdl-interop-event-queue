package io.accelerate.events.interop.queue.clitool.cmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.accelerate.events.interop.queue.connector.EventProcessingException;
import io.accelerate.events.interop.queue.connector.EventSerializationException;
import io.accelerate.events.interop.queue.connector.QueueEvent;
import io.accelerate.events.interop.queue.connector.SqsEventQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SendEventCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(SendEventCommand.class);

    public static final Map<String, Class> eventNameToEventType;
    static {
        eventNameToEventType = new HashMap<>();
        Reflections reflections = new Reflections("tdl.participant.queue.events");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(QueueEvent.class);
        annotated.forEach(theClass -> {
            QueueEvent annotation = theClass.getAnnotation(QueueEvent.class);
            SendEventCommand.eventNameToEventType.put(annotation.name(), theClass);
        });
    }

    private static final int EVENT_NAME_AS_PARAMETER = 1;
    private static final int KEY_VALUE_PAIRS_AS_PARAMETER = 2;
    private SqsEventQueue sqsEventQueue;

    @SuppressWarnings("WeakerAccess")
    public SendEventCommand(SqsEventQueue sqsEventQueue) {
        this.sqsEventQueue = sqsEventQueue;
    }

    @Override
    public boolean execute(String[] args, boolean dryRun) throws Exception {
        if (args.length > 2) {
            String eventName = args[EVENT_NAME_AS_PARAMETER];
            Object message;
            String messageBodyAsJson = buildJsonFromTheRestOfThe(args);
            message = getEventObject(eventName, messageBodyAsJson);
            if (message == null) {
                return false;
            }

            // Print
            log.info(message.toString());

            // Send
            if (dryRun) {
                log.warn("!!~~~~~~~ This was a dry run. Set DRY_RUN=false if you want to apply the changes. ~~~~~~~!!");
            } else {
                sendEventToSQS(message, sqsEventQueue);
                log.info("{} event with attributes ('{}') was successfully sent.%n", eventName, message);
            }
            return true;
        } else {
            log.error("Incorrect number of parameters passed, please refer to Usage text.");
        }
        return false;
    }


    //~~~~~~ Helpers

    private static String buildJsonFromTheRestOfThe(String[] args) {
        Map<String, String> valueMap = new HashMap<>();
        for (int index = KEY_VALUE_PAIRS_AS_PARAMETER; index < args.length; index++) {
            String[] splitEachToken = args[index].split("=");
            String key = splitEachToken[0];
            String value = splitEachToken[1];
            valueMap.put(key, value);
        }

        //Replace timestamp token
        String timestampKey = "timestampMillis";
        if ("NOW".equals(valueMap.getOrDefault(timestampKey, "NOW"))) {
            valueMap.put(timestampKey, System.currentTimeMillis() + "");
        }

        return valueMap.entrySet().stream()
                .map(entry -> String.format("\"%s\": \"%s\"", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining(", ","{","}"));
    }

    private static Object getEventObject(String eventName, String messageBody) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readerFor(eventNameToEventType.get(eventName)).readValue(messageBody);
    }

    private static void sendEventToSQS(Object message, SqsEventQueue sqsEventQueue) throws EventSerializationException, EventProcessingException {
        sqsEventQueue.send(message);
    }

}
