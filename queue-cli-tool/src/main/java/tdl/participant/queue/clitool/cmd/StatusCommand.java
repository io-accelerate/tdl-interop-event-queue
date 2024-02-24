package tdl.participant.queue.clitool.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdl.participant.queue.connector.QueueSize;
import tdl.participant.queue.connector.SqsEventQueue;

public class StatusCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(StatusCommand.class);

    private SqsEventQueue sqsEventQueue;

    @SuppressWarnings("WeakerAccess")
    public StatusCommand(SqsEventQueue sqsEventQueue) {
        this.sqsEventQueue = sqsEventQueue;
    }

    @Override
    public boolean execute(String[] args, boolean dryRun) {
        QueueSize queueSize = sqsEventQueue.getQueueSize();
        log.info("The queue '{}' contains: " +
                        "'{}' available messages, " +
                        "'{}' notVisible messages, " +
                        "'{}' delayed messages",
                sqsEventQueue.getQueueUrl(),
                queueSize.available(),
                queueSize.notVisible(),
                queueSize.delayed()
        );
        return true;
    }
}
