package tdl.participant.queue.clitool.cmd;

public interface Command {
    boolean execute(String[] args, boolean dryRun) throws Exception;
}
