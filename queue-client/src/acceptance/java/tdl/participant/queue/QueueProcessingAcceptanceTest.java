package tdl.participant.queue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import tdl.participant.queue.connector.QueueEvent;
import tdl.participant.queue.connector.QueueEventHandlers;
import tdl.participant.queue.connector.QueueSize;
import tdl.participant.queue.connector.SqsEventQueue;
import tdl.participant.queue.events.ChallengeStartedEvent;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueProcessingAcceptanceTest {
    private AmazonSQS client;
    private String queueUrl;
    private String blockingQueueUrl;

    @BeforeEach
    public void setUp() {
        client = testAwsClient();
        queueUrl = "http://localhost:9324/queue/acceptance-test";
        blockingQueueUrl = "http://127.0.0.99:19432";
    }

    @Test
    public void should_submit_and_consume_valid_request() throws Exception {
        // Given
        SqsEventQueue sqsEventQueue = initQueue(queueUrl);
        List<Object> afterEvents = new ArrayList<>();
        List<Object> processedEvents = new ArrayList<>();
        List<Object> beforeEvents = new ArrayList<>();
        QueueEventHandlers queueEventHandlers = new QueueEventHandlers();
        queueEventHandlers.before((eventName, eventVersion, eventObject) -> beforeEvents.add(eventObject));
        queueEventHandlers.on(ChallengeStartedEvent.class, processedEvents::add);
        queueEventHandlers.after((eventName, eventVersion, eventObject) -> afterEvents.add(eventObject));
        // When
        sendRaw("ignore");
        sqsEventQueue.send(new UnknownEvent("unknown event"));
        sqsEventQueue.send(new ChallengeStartedEvent(123,"x", "y"));
        sqsEventQueue.subscribeToMessages(queueEventHandlers);
        // Then messages have been processed
        Thread.sleep(500);
        assertThat(sqsEventQueue.getQueueSize(), is(new QueueSize(0, 2, 0)));
        sqsEventQueue.unsubscribeFromMessages();
        // And the handlers have been called
        assertThat(processedEvents.size(), equalTo(1));
        assertThat(processedEvents.getFirst(), instanceOf(ChallengeStartedEvent.class));
        // Before and after where called
        assertThat(afterEvents, equalTo(processedEvents));
        assertThat(beforeEvents, equalTo(processedEvents));
    }

    @Test
    @Timeout(2)
    public void should_not_block_if_queue_not_available() {
        SqsEventQueue sqsEventQueue = new SqsEventQueue(client, blockingQueueUrl);
        try {
            sqsEventQueue.send(new UnknownEvent("unknown event"),
                    1000, 1000);
        } catch (Exception e) {
            //Ignored
        }
    }

    private static AmazonSQS testAwsClient() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:9324", "eu-west-2");
        return AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("x", "x")))
                .build();
    }

    private SqsEventQueue initQueue(String queueUrl) {
        client.purgeQueue(new PurgeQueueRequest(queueUrl));
        SqsEventQueue sqsEventQueue = new SqsEventQueue(client, this.queueUrl);
        assertThat("Queue "+queueUrl+" is not clean.", sqsEventQueue.getQueueSize(),
                is(new QueueSize(0, 0, 0)));
        return sqsEventQueue;
    }

    @SuppressWarnings("SameParameterValue")
    private void sendRaw(String message) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.setQueueUrl(queueUrl);
        sendMessageRequest.setMessageBody(message);
        client.sendMessage(sendMessageRequest);
    }

    @QueueEvent(name = "unknown", version = "0.1")
        private record UnknownEvent(String text) {
    }
}