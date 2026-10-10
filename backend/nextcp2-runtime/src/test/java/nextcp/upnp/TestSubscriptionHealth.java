package nextcp.upnp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestSubscriptionHealth {
    @Test
    void actualEventsExpireAndAnEndedSubscriptionIsNotHealthy() {
        AtomicLong clock = new AtomicLong();
        SubscriptionHealth health = new SubscriptionHealth(clock::get);
        assertFalse(health.isReceivingEvents());
        health.eventReceived();
        assertTrue(health.hasRecentEvents(5000));
        clock.set(TimeUnit.SECONDS.toNanos(5));
        assertFalse(health.hasRecentEvents(5000));
        assertTrue(health.isReceivingEvents(), "Quiet metadata subscriptions are still established");
        health.failed();
        assertFalse(health.isReceivingEvents());
        health.eventReceived();
        assertTrue(health.hasRecentEvents(5000));
    }
}
