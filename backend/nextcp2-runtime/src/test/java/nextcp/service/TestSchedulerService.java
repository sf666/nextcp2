package nextcp.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestSchedulerService {
    @Test
    void aBlockedRendererDoesNotBlockOtherDevicesOrOverlapItsOwnTicks() throws Exception {
        SchedulerService scheduler = new SchedulerService();
        CountDownLatch entered = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        CountDownLatch healthy = new CountDownLatch(2);
        AtomicInteger calls = new AtomicInteger();
        scheduler.addNotifierOneSecond(counter -> {
            calls.incrementAndGet();
            entered.countDown();
            try { release.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        scheduler.addNotifierOneSecond(counter -> healthy.countDown());
        try {
            scheduler.oneSecondTicker();
            assertTrue(entered.await(2, TimeUnit.SECONDS));
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
            while (healthy.getCount() == 2 && System.nanoTime() < deadline) Thread.sleep(5);
            assertEquals(1, healthy.getCount());
            // Wait for the first short callback to leave its in-flight guard.
            Thread.sleep(20);
            scheduler.oneSecondTicker();
            assertTrue(healthy.await(2, TimeUnit.SECONDS));
            assertEquals(1, calls.get());
        } finally {
            release.countDown();
            scheduler.destroy();
        }
    }

    @Test
    void removingADeviceStopsFutureUpdates() throws Exception {
        SchedulerService scheduler = new SchedulerService();
        AtomicInteger calls = new AtomicInteger();
        ISchedulerService notifier = counter -> calls.incrementAndGet();
        scheduler.addNotifierOneSecond(notifier);
        scheduler.removeNotifierOneSecond(notifier);
        try {
            scheduler.oneSecondTicker();
            Thread.sleep(30);
            assertEquals(0, calls.get());
        } finally {
            scheduler.destroy();
        }
    }
}
