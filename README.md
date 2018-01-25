# tdl-interop-event-queue
The event queue client

# To fully test and build

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

