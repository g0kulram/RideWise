package com.airtribe.ridewise.util;

import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {

    private static final AtomicInteger riderCounter = new AtomicInteger(0);
    private static final AtomicInteger driverCounter = new AtomicInteger(0);
    private static final AtomicInteger rideCounter = new AtomicInteger(0);

    private IdGenerator() {
        // utility class — no instances
    }

    public static String nextRiderId() {
        return "R-" + riderCounter.incrementAndGet();
    }

    public static String nextDriverId() {
        return "D-" + driverCounter.incrementAndGet();
    }

    public static String nextRideId() {
        return "RIDE-" + rideCounter.incrementAndGet();
    }
}
