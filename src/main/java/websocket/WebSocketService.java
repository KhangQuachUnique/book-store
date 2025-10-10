package websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/updates")
public class WebSocketService {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("✅ Connected: " + session.getId() + ", | Active connections: " + sessions.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("💬 Received: " + message);
        // Gửi lại cho tất cả client
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText("User " + session.getId() + ": " + message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("❌ Disconnected: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("⚠️ Error: " + throwable.getMessage());
    }

    // Java
    public static boolean isSessionConnected(Session session) {
        return sessions.contains(session);
    }

    // To get the number of active connections:
    public static int getActiveConnectionCount() {
        return sessions.size();
    }

}
