package net.artresearch.api.service;

import gr.forth.ics.isl.x3ml.X3MLEngineFactory.OutputFormat;
import net.artresearch.api.model.X3MLSessionRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class X3MLService {
    private static final ConcurrentHashMap<String, X3MLSession> sessions = new ConcurrentHashMap<>();

    public X3MLSession createSession(X3MLSessionRequest request) throws IOException {
        X3MLSession session = new X3MLSession(request.getSessionId(), request.getMappingsContent(), request.getPolicyContent(), request.getOutputFormat());
        sessions.put(request.getSessionId(), session);
        X3MLSessionManager.scheduleSessionExpiry(request.getSessionId(), sessions);
        return session;
    }

    public void removeSession(String sessionId) {
        X3MLSession session = sessions.remove(sessionId);
        if (session != null) {
            session.cleanUp();
        }
    }

    public X3MLSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}

