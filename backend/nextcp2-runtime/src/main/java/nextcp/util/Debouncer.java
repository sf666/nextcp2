package nextcp.util;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A utility for debouncing function calls.
 */
public class Debouncer<T> {

    private final ScheduledExecutorService executorService;
    private final ConcurrentHashMap<Object, DebouncedTask<T>> buffer = new ConcurrentHashMap<>();
    private final Duration debounceDuration;

    public Debouncer(ScheduledExecutorService executorService, Duration debounceDuration) {
        this.executorService = executorService;
        this.debounceDuration = debounceDuration;
    }

    /**
     * Submit a task to the Debouncer. A task is identified by the {@code identifier} parameter's
     * {@link Object#equals(Object)} method. If a second task with an equal identifier is submitted before
     * {@code debounceDuration} is over, the previous task will be canceled and replaced by the new one. Effectively,
     * the last callback "wins". As a consequence of this, it cannot be guaranteed that a submitted task will be
     * executed.
     *
     * @param identifier the identifier of the task by which duplication is detected. Identity is defined by the
     *        object's {@code equals(Object)} method.
     * @param task the task to debounce
     * @return A future which is completed when the task was executed
     */
    public Future<?> submit(Object identifier, Runnable task) {
        return submit(identifier, () -> {
            task.run();
            return null;
        });
    }

    /**
     * See {@link Debouncer#submit(Object, Runnable)}.
     */
    public Future<T> submit(Object identifier, Callable<T> task) {
        return buffer.compute(identifier, (k, debouncedTask) -> {
            if (debouncedTask != null) {
                debouncedTask.scheduledFuture.cancel(false);
            } else {
                Instant deadline = Instant.now().plus(debounceDuration);
                debouncedTask = new DebouncedTask<>(deadline);
            }
            long remainingMillis = millisUntil(debouncedTask.deadline);
            debouncedTask.scheduledFuture = executorService.schedule(() -> runAndCleanUp(identifier, task),
                    remainingMillis, MILLISECONDS);
            return debouncedTask;
        }).finalFuture;
    }

    private void runAndCleanUp(Object key, Callable<T> task) {
        DebouncedTask<T> debouncedTask = buffer.remove(key);
        if (debouncedTask == null) {
            throw new IllegalStateException("Tried to run a debounce task that doesn't exist. This is a bug.");
        }
        try {
            debouncedTask.finalFuture.complete(task.call());
        } catch (Exception e) {
            debouncedTask.finalFuture.completeExceptionally(e);
        }
    }

    private long millisUntil(Instant target) {
        return Math.max(0, Duration.between(Instant.now(), target).toMillis());
    }


    private static class DebouncedTask<T> {
    	private DebouncedTask(Instant deadline) {
    		this.deadline = deadline;
    	}
    	
        private final CompletableFuture<T> finalFuture = new CompletableFuture<>();
        private final Instant deadline;
        private Future<?> scheduledFuture;
    }



    // Package private getter for testing
    ConcurrentHashMap<Object, DebouncedTask<T>> getBuffer() {
        return buffer;
    }
}