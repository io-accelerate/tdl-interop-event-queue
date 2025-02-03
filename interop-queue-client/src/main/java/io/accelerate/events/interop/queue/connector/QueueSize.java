package io.accelerate.events.interop.queue.connector;

public record QueueSize(int available, int notVisible, int delayed) {
}
