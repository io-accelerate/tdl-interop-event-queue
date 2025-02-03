[![Java Version](http://img.shields.io/badge/Java-1.8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![Download](https://api.bintray.com/packages/julianghionoiu/maven/tdl-interop-event-queue/images/download.svg)](https://bintray.com/julianghionoiu/maven/tdl-interop-event-queue/_latestVersion)
[![Codeship Status for julianghionoiu/dev-screen-record](https://img.shields.io/codeship/f4e468f0-e403-0135-2d6a-3e0434e5c2c3/master.svg)](https://codeship.com/projects/268708)


## To use as a library


### Add as Maven dependency

Add a dependency to `io.accelerate:interop-queue-client` in `compile` scope.
```xml
<dependency>
  <groupId>io.accelerate</groupId>
  <artifactId>interop-queue-client</artifactId>
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
  queueUrl = "http://localhost:9324/queue/participant-events"
}
```

Ensure that your environment has the right AWS credentials.
If not, you can set the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_KEY` environment variables.

### Run QueueCLITool

```bash
DRY_RUN=true java -Dconfig.file=.private/aws-queue-cli.conf -jar ./interop-queue-cli-tool/build/libs/interop-queue-cli-tool-*-all.jar [command] [args to command]
```

For usage and examples, run the below:
```bash
DRY_RUN=true java -Dconfig.file=.private/aws-queue-cli.conf -jar ./interop-queue-cli-tool/build/libs/interop-queue-cli-tool-*-all.jar
```


## To fully test and build

Run unit tests
```bash
./gradlew test -i
```

Start external dependencies
```bash
python3 ./local-sqs/elasticmq-wrapper.py start
```

Create runnable artifacts
```bash
./gradlew clean shadowJar -i
```

Run tests
```bash
./gradlew acceptanceTest -i
```

Stop external dependencies
```bash
python3 ./local-sqs/elasticmq-wrapper.py stop
```

### Release

Configure the version inside the "gradle.properties" file

Create publishing bundle into Maven Local
```bash
./gradlew publishToMavenLocal
```

Check Maven Local contains release version:
```
CURRENT_VERSION=$(cat gradle.properties | grep version | cut -d "=" -f2)

ls -l $HOME/.m2/repository/io/accelerate/interop-queue-client/${CURRENT_VERSION}
```

Publish to Maven Central Staging repo

### Publish to Maven Central - the manual way

At this point publishing to Maven Central from Gradle is only possible manually.
Things might have changed, check this page:
https://central.sonatype.org/publish/publish-portal-gradle/

Generate the Maven Central bundle:
```
./generateMavenCentralBundle.sh
```

Upload the bundle to Maven Central by clicking the "Publish Component" button.
https://central.sonatype.com/publishing
