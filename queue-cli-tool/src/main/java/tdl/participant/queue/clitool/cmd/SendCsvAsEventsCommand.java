package tdl.participant.queue.clitool.cmd;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdl.participant.queue.connector.SqsEventQueue;
import tdl.participant.queue.events.*;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static tdl.participant.queue.clitool.cmd.TypeConversion.*;

public class SendCsvAsEventsCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(SendCsvAsEventsCommand.class);
    private SqsEventQueue sqsEventQueue;

    private static final int CSV_FILE_PATH = 1;

    public SendCsvAsEventsCommand(SqsEventQueue sqsEventQueue) {
        this.sqsEventQueue = sqsEventQueue;
    }

    @Override
    public boolean execute(String[] args, boolean dryRun) throws Exception {
        if (args.length > 1) {
            String fileName = args[CSV_FILE_PATH];
            log.info("Reading data from CSV file: "+fileName);

            Reader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

            // Transform to events
            List<Object> events = new ArrayList<>();
            for (CSVRecord record : records) {
                events.addAll(recordToQueueEvents(record));
            }

            // Print all
            events.stream().map(Object::toString).forEach(log::info);

            // Send all
            if (dryRun) {
                log.warn("!!~~~~~~~ This was a dry run. Set DRY_RUN=false if you want to apply the changes. ~~~~~~~!!");
            } else {
                for (Object event : events) {
                    sqsEventQueue.send(event);
                }
            }
            return true;
        } else {
            log.error("Incorrect number of parameters passed, please refer to Usage text.");
            return false;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private List<Object> recordToQueueEvents(CSVRecord record) {
        List<Object> events = new ArrayList<>();

        //Setup
        String challengeId = record.get("Challenge");
        String group = record.get("Group");
        String participant = record.get("Participant");
        log.info("Processing record for: {} -> {}", group, participant);

        //Challenge Started / Completed
        asDate(record.get("DateStarted")).ifPresent(
                timestampSec -> events.add(new ChallengeStartedEvent(asMillis(timestampSec), participant, challengeId)));
        asDate(record.get("DateCompleted")).ifPresent(
                timestampSec -> events.add(new ChallengeCompletedEvent(asMillis(timestampSec),
                        participant, challengeId, 0, 0)));


        //Rounds
        int numRounds = 5;
        for (int i = 1; i <= numRounds; i++) {
            String roundId = challengeId + "_R" + i;

            Optional<Integer> clockTimeMin = asInt(record.get("R" + i));
            Optional<Integer> penaltyTimeMin = asInt(record.get("X" + i));

            clockTimeMin.ifPresent(clockTime -> penaltyTimeMin.ifPresent(
                    penalties -> events.add(new RoundCompletedEvent(now(),participant, roundId, clockTime, penalties))));

            Optional<Integer> coverage = asInt(record.get("C" + i));
            coverage.ifPresent((c) -> events.add(new CoverageComputedEvent(now(), participant, roundId, c)));
        }

        asUrl(record.get("SourceCode")).ifPresent(
                sourceCodeLink -> events.add(new SourceCodeUpdatedEvent(now(), participant, challengeId, sourceCodeLink)));
        asUrl(record.get("Screencast")).ifPresent(
                screenCastLink -> events.add(new AnonymisedVideoUpdatedEvent(now(), participant, challengeId, screenCastLink)));
        asString(record.get("Language")).ifPresent(
                programmingLanguage -> events.add(new ProgrammingLanguageDetectedEvent(now(), participant, challengeId, programmingLanguage)));

        return events;
    }

    private static long now() {
        return System.currentTimeMillis();
    }




}
