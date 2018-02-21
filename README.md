[![Java Version](http://img.shields.io/badge/Java-1.8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![Download](https://api.bintray.com/packages/julianghionoiu/maven/tdl-interop-event-queue/images/download.svg)](https://bintray.com/julianghionoiu/maven/tdl-interop-event-queue/_latestVersion)
[![Codeship Status for julianghionoiu/dev-screen-record](https://img.shields.io/codeship/f4e468f0-e403-0135-2d6a-3e0434e5c2c3/master.svg)](https://codeship.com/projects/268708)


## To use as a library


### Add as Maven dependency

Add a dependency to `ro.ghionoiu:queue-client` in `compile` scope. See `bintray` shield for latest release number.
```xml
<dependency>
  <groupId>ro.ghionoiu</groupId>
  <artifactId>queue-client</artifactId>
  <version>0.1.10</version>
  <type>pom</type>
</dependency>
```

Initialise in your app
```java
        // Initialise event queue
        AmazonSQS client;
        String queueUrl;
        SqsEventQueue sqsEventQueue = new SqsEventQueue(client, queueUrl);
```


### To use as Producer

The queue can be used to send classes annotated as QueueEvent
```java
        sqsEventQueue.send(new SomeEvent("ok"));
```

Where:
```java

        @QueueEvent(name = "someEvent", version = "0.1")
        private class SomeEvent {
            private String text;

            SomeEvent(String text) {
                this.text = text;
            }

            public String getText() {
                return text;
            }
        }

```

### To use as Consumer

Register event handlers
```java
        List<Object> capturedEvents = new ArrayList<>();
        QueueEventHandlers queueEventHandlers = new QueueEventHandlers();
        queueEventHandlers.put(ChallengeStartedEvent.class, capturedEvents::add);
```

Start consuming messages by subscribing to the queue, the process will detach at this point
```java
        sqsEventQueue.subscribeToMessages(queueEventHandlers);
```

When done, stop consuming the messages
```java
        sqsEventQueue.unsubscribeFromMessages();
```


## As a command line Producer

### Download artifact from Github

See
https://github.com/julianghionoiu/tdl-interop-event-queue/releases

### Configure

Create a `.private/local-acceptance.conf` that contains the SQS configuration:
```
sqs {
  serviceEndpoint = "http://localhost:9324"
  signingRegion = "eu-west-2"
  accessKey = "x"
  secretKey = "y"
  queueUrl = "http://localhost:9324/queue/participant-events"
}
```

### Run QueueCLITool

```bash
DRY_RUN=true java -Dconfig.file=.private/aws-queue-cli.conf -jar ./queue-cli-tool/build/libs/queue-cli-tool-*-all.jar [command] [args to command]
```

For usage and examples, run the below:
```bash
DRY_RUN=true java -Dconfig.file=.private/aws-queue-cli.conf -jar ./queue-cli-tool/build/libs/queue-cli-tool-*-all.jar
```


## To fully test and build

Run unit tests
```bash
./gradlew test -i
```

Start external dependencies
```bash
python ./local-sqs/elasticmq-wrapper.py start
```

Create runnable artifacts
```bash
./gradlew clean shadowJar -i
```

Run tests
```bash
./gradlew acceptanceTest -i
```


### Release library to jcenter and mavenCentral

The CI server is configured to pushs release branches to Bintray.
You trigger the process by running the `release` command locally.

The command will increment the release number and create and annotated tag:
```bash
./gradlew release
git push --all
git push --tags
```