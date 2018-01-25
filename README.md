[![Java Version](http://img.shields.io/badge/Java-1.8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![Download](https://api.bintray.com/packages/julianghionoiu/maven/tdl-interop-event-queue/images/download.svg)](https://bintray.com/julianghionoiu/maven/tdl-interop-event-queue/_latestVersion)
[![Codeship Status for julianghionoiu/dev-screen-record](https://img.shields.io/codeship/f4e468f0-e403-0135-2d6a-3e0434e5c2c3/master.svg)](https://codeship.com/projects/268708)


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


### Release to jcenter and mavenCentral

The CI server is configured to pushs release branches to Bintray.
You trigger the process by running the `release` command locally.

The command will increment the release number and create and annotated tag:
```bash
./gradlew release
git push --all
git push --tags
```