package tdl.participant.queue.connector;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SqsEventQueue {
    private static final Logger log = LoggerFactory.getLogger(SqsEventQueue.class);
    private static final String ATTRIBUTE_EVENT_NAME = "name";
    private static final String ATTRIBUTE_EVENT_VERSION = "version";
    private final AmazonSQS client;
    private final String queueUrl;
    private final ObjectMapper mapper;
    private MessageProcessingThread messageProcessingThread;

    public SqsEventQueue(AmazonSQS client, String queueUrl) {
        this.client = client;
        this.queueUrl = queueUrl;
        this.mapper = new ObjectMapper();
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    public QueueSize getQueueSize() {
        int available = Integer.parseInt(getQueueAttribute("ApproximateNumberOfMessages"));
        int notVisible = Integer.parseInt(getQueueAttribute("ApproximateNumberOfMessagesNotVisible"));
        int delayed = Integer.parseInt(getQueueAttribute("ApproximateNumberOfMessagesDelayed"));
        return new QueueSize(available, notVisible, delayed);
    }

    private String getQueueAttribute(String attributeName) {
        GetQueueAttributesResult queueAttributes = client
                .getQueueAttributes(queueUrl, Collections.singletonList(attributeName));
        return queueAttributes.getAttributes().get(attributeName);
    }

    private record asdasdg(GetQueueAttributesResult queueAttributes, String queueAttribute) {
    }

    public void send(Object object) throws EventSerializationException, EventProcessingException {
        send(object, 10000, 5000);
    }


    public void send(Object object, int sdkClientExecutionTimeout, int sdkRequestTimeout) throws EventSerializationException, EventProcessingException {
        QueueEvent annotation = object.getClass().getAnnotation(QueueEvent.class);
        if (annotation == null) {
            throw new EventSerializationException(object.getClass()+" not a QueueEvent");
        }
        String eventName = annotation.name();
        String eventVersion = annotation.version();

        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.setSdkClientExecutionTimeout(sdkClientExecutionTimeout);
            sendMessageRequest.setSdkRequestTimeout(sdkRequestTimeout);
            sendMessageRequest.setQueueUrl(queueUrl);
            sendMessageRequest.setMessageBody(mapper.writeValueAsString(object));
            sendMessageRequest.addMessageAttributesEntry(ATTRIBUTE_EVENT_NAME,
                    new MessageAttributeValue().withDataType("String").withStringValue(eventName));
            sendMessageRequest.addMessageAttributesEntry(ATTRIBUTE_EVENT_VERSION,
                    new MessageAttributeValue().withDataType("String").withStringValue(eventVersion));
            client.sendMessage(sendMessageRequest);
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Failed to serialize event of type "+object.getClass(), e);
        } catch (SdkClientException e) {
            throw new EventProcessingException("Failed to send message due to connectivity issues.", e);
        }
    }

    public void subscribeToMessages(QueueEventHandlers eventHandlers) {
        messageProcessingThread = new MessageProcessingThread(client, queueUrl, mapper, eventHandlers);
        messageProcessingThread.start();
    }

    static class MessageProcessingThread extends Thread {
        static final int MAX_NUMBER_OF_MESSAGES = 10;
        static final int MAX_AWS_WAIT = 20;

        private final ReceiveMessageRequest receiveMessageRequest;
        private final AmazonSQS client;
        private final ObjectMapper mapper;
        private final int maxProcessingThreads = 10;
        private final ExecutorService executorService;
        private final DeleteMessageBatchRequest deleteMessageBatchRequest;
        private final AtomicBoolean shouldContinue;
        private final QueueEventHandlers eventHandlers;

        MessageProcessingThread(AmazonSQS client,
                                String queueUrl,
                                ObjectMapper mapper,
                                QueueEventHandlers eventHandlers) {
            this.client = client;
            this.mapper = mapper;
            this.eventHandlers = eventHandlers;

            receiveMessageRequest = new ReceiveMessageRequest();
            receiveMessageRequest.setMaxNumberOfMessages(MAX_NUMBER_OF_MESSAGES);
            receiveMessageRequest.setQueueUrl(queueUrl);
            receiveMessageRequest.setWaitTimeSeconds(MAX_AWS_WAIT);
            receiveMessageRequest.setMessageAttributeNames(Arrays.asList(ATTRIBUTE_EVENT_NAME, ATTRIBUTE_EVENT_VERSION));

            deleteMessageBatchRequest = new DeleteMessageBatchRequest();
            deleteMessageBatchRequest.setQueueUrl(queueUrl);

            //noinspection Convert2Diamond
            executorService = new ThreadPoolExecutor(1, maxProcessingThreads,
                    60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    Executors.defaultThreadFactory());

            shouldContinue = new AtomicBoolean(true);
        }

        @Override
        public void run() {
            while (shouldContinue.get()) {
                processBatch();
            }
        }

        void signalStop() throws InterruptedException {
            shouldContinue.set(false);
            client.shutdown();

            // Orderly shutdown
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        }

        void processBatch() {
            CompletionService<Message> completionService =
                    new ExecutorCompletionService<>(executorService);

            // Fetch messages from SQS
            List<Message> messages = client.receiveMessage(receiveMessageRequest).getMessages();

            // Dispatch workers for processing
            messages.forEach(message -> completionService.submit(() -> process(message)));

            // Wait for completion
            int completed = 0;
            List<Message> successfulMessages = new ArrayList<>();
            while(completed < messages.size()) {
                try {
                    Future<Message> resultFuture = completionService.take();
                    successfulMessages.add(resultFuture.get());
                } catch (Exception e) {
                    log.error("Failed to process queue message", e);
                } finally {
                    completed++;
                }
            }

            // Delete processed messages
            if (successfulMessages.size() > 0) {
                List<DeleteMessageBatchRequestEntry> deleteMessageEntries = successfulMessages.stream().map(message ->
                        new DeleteMessageBatchRequestEntry(message.getMessageId(), message.getReceiptHandle()))
                        .collect(Collectors.toList());

                deleteMessageBatchRequest.setEntries(deleteMessageEntries);
                client.deleteMessageBatch(deleteMessageBatchRequest);
            }
        }

        private Message process(Message message) throws EventDeserializationException, EventProcessingException {
            // Verify message validity
            if (!message.getMessageAttributes().containsKey(ATTRIBUTE_EVENT_NAME)) {
                throw new EventDeserializationException("Message does not contain the " + ATTRIBUTE_EVENT_NAME + " attribute");
            }
            if (!message.getMessageAttributes().containsKey(ATTRIBUTE_EVENT_VERSION)) {
                throw new EventDeserializationException("Message does not contain the " + ATTRIBUTE_EVENT_VERSION + " attribute");
            }
            log.debug("Attributes for message:" +message.getBody() + " -> "+message.getMessageAttributes().entrySet());

            String eventName = message.getMessageAttributes().get(ATTRIBUTE_EVENT_NAME).getStringValue();
            String eventVersion = message.getMessageAttributes().get(ATTRIBUTE_EVENT_VERSION).getStringValue();
            if(!eventHandlers.canHandle(eventName, eventVersion)) {
                throw new EventProcessingException("No event handlers for: "+eventName+", version: "+eventVersion);
            }

            // Handle
            HandleRule handleRule = eventHandlers.getHandleRuleFor(eventName, eventVersion);
            try {
                Object object = mapper.readerFor(handleRule.getType()).readValue(message.getBody());
                eventHandlers.getBeforeEventInspector().inspect(eventName, eventVersion, object);
                //noinspection unchecked
                handleRule.getConsumer().accept(object);
                eventHandlers.getAfterEventInspector().inspect(eventName, eventVersion, object);
                return message;
            } catch (IOException e) {
                throw new EventDeserializationException("Cannot deserialize message into an instance of "
                        + handleRule.getType()+"."+message.getBody(), e);
            } catch (Exception e) {
                throw new EventProcessingException("Exception while consuming individual message: "+message.getBody(), e);
            }
        }
    }

    public void unsubscribeFromMessages() throws InterruptedException {
        if (messageProcessingThread == null) {
            throw new IllegalStateException("Cannot unsubscribe without first being subscribed");
        }

        messageProcessingThread.signalStop();
        messageProcessingThread.join();
    }
}
