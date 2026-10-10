package nextcp.upnp;

import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

/** Tracks actual GENA delivery separately from action reads and subscription acknowledgements. */
public final class SubscriptionHealth {

    private final LongSupplier clock;
    private volatile boolean subscribed;
    private volatile boolean received;
    private volatile long lastEvent;

    public SubscriptionHealth() {
        this(System::nanoTime);
    }

    SubscriptionHealth(LongSupplier clock) {
        this.clock = clock;
    }

    public void eventReceived() {
        lastEvent = clock.getAsLong();
        received = true;
        subscribed = true;
    }

    public void failed() {
        subscribed = false;
    }

    public boolean isReceivingEvents() {
        return subscribed && received;
    }

    public boolean hasRecentEvents(long maximumAgeMs) {
        return isReceivingEvents() && clock.getAsLong() - lastEvent < TimeUnit.MILLISECONDS.toNanos(maximumAgeMs);
    }
}
