package io.accelerate.events.interop.queue.clitool;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.accelerate.events.interop.queue.clitool.cmd.Command;
import io.accelerate.events.interop.queue.clitool.cmd.SendCsvAsEventsCommand;
import io.accelerate.events.interop.queue.clitool.cmd.SendEventCommand;
import io.accelerate.events.interop.queue.clitool.cmd.StatusCommand;
import io.accelerate.events.interop.queue.connector.SqsEventQueue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueueCLITool {

    public static void main(String[] args) throws Exception {
        Config config = ConfigFactory.load();
        config.checkValid(ConfigFactory.defaultReference());

        AmazonSQS client = createAWSClient(
                config.getString("sqs.serviceEndpoint"),
                config.getString("sqs.signingRegion")
        );

        String queueUrl = config.getString("sqs.queueUrl");
        SqsEventQueue sqsEventQueue = new SqsEventQueue(client, queueUrl);

        //Commands
        Map<String, Command> commandMap = new HashMap<>();
        commandMap.put("send", new SendEventCommand(sqsEventQueue));
        commandMap.put("sendCSV", new SendCsvAsEventsCommand(sqsEventQueue));
        commandMap.put("status", new StatusCommand(sqsEventQueue));


        //Dry run?
        boolean dryRun = asBool(System.getenv("DRY_RUN"), true);


        boolean actionCompleted = false;
        if (args.length > 0 && commandMap.containsKey(args[0])) {
            actionCompleted = commandMap.get(args[0]).execute(args, dryRun);
        }

        if (!actionCompleted) {
            printUsageText();
        }
    }

    private static void printUsageText() {
        String eventTypes = SendEventCommand.eventNameToEventType.entrySet().stream()
                .map(pair -> pair.getKey() + " - " + Arrays.stream(pair.getValue().getDeclaredFields())
                        .map(Field::getName)
                        .collect(Collectors.joining(", ")))
                .sorted()
                .collect(Collectors.joining("\n"));
        System.out.println(
                "\n" +
                        "Usage: \n" +
                        "      QueueCLITool send <eventType> <event arguments> \n" +
                        "      QueueCLITool sendCSV <pathToCSV> \n" +
                        "      QueueCLITool status \n" +
                        "\n" +
                        "   For example: \n" +
                        "      QueueCLITool send challengeStarted timestampMillis=NOW participant=X challengeId=CHK timestampSec=1\n" +
                        "      QueueCLITool send challengeCompleted timestampMillis=NOW participant=X challengeId=CHK timestampSec=1 totalClockTimeMin=2 totalPenaltyTimeMin=3\n" +
                        "\n" +
                        "List of event types\n" +
                        "-------------------\n" +
                        eventTypes + "\n"
        );
    }

    private static AmazonSQS createAWSClient(String serviceEndpoint, String signingRegion) {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion);
        return AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }


    @SuppressWarnings("SameParameterValue")
    private static Boolean asBool(String value, Boolean defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        return Boolean.valueOf(value);
    }

}
