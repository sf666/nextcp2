package nextcp.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerService
{
    private long counter = 0;

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final Map<ISchedulerService, AtomicBoolean> notifyListOneSecond = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public SchedulerService()
    {
    }

    public void addNotifierOneSecond(ISchedulerService notifier)
    {
        notifyListOneSecond.computeIfAbsent(notifier, key -> new AtomicBoolean());
    }

    public void removeNotifierOneSecond(ISchedulerService notifier)
    {
        notifyListOneSecond.remove(notifier);
    }

    @Scheduled(fixedRate = 1000)
    public void oneSecondTicker()
    {
        counter++;
        long tick = counter;
        notifyListOneSecond.forEach((notifier, running) -> {
            if (!running.compareAndSet(false, true)) {
                return;
            }
            // A slow or offline renderer must not stall every other device's watchdog.
            executor.execute(() -> {
                try {
                    if (notifyListOneSecond.get(notifier) == running) {
                        notifier.tick(tick);
                    }
                } catch (Exception e) {
                    log.debug("Device state update failed", e);
                } finally {
                    running.set(false);
                }
            });
        });
    }

    @PreDestroy
    void destroy()
    {
        notifyListOneSecond.clear();
        executor.shutdownNow();
    }

}
