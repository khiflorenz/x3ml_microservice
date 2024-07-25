package net.artresearch.api.service;

import java.util.concurrent.*;

public class X3MLSessionManager {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void scheduleSessionExpiry(String sessionId, ConcurrentHashMap<String, X3MLSession> sessions) {
        scheduler.schedule(() -> {
            X3MLSession session = sessions.remove(sessionId);
            if (session != null) {
                session.cleanUp();
            }
        }, 2, TimeUnit.HOURS);
    }
}

