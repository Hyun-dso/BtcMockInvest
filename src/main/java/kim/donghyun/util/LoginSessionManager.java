package kim.donghyun.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

/**
 * Manages currently logged in user sessions to prevent duplicate logins.
 */
@Component
public class LoginSessionManager {
    private static final Map<Long, HttpSession> sessions = new ConcurrentHashMap<>();

    /**
     * Registers a session for the given user ID.
     *
     * @param userId  the user ID
     * @param session the HTTP session
     * @return {@code true} if registration succeeded, or {@code false} if the user is already logged in
     */
    public static synchronized boolean registerUser(Long userId, HttpSession session) {
        if (sessions.containsKey(userId)) {
            return false;
        }
        sessions.put(userId, session);
        return true;
    }

    /**
     * Removes the session for the given user ID.
     *
     * @param userId the user ID
     */
    public static synchronized void removeUser(Long userId) {
        sessions.remove(userId);
    }

    /**
     * Checks whether the user is already logged in.
     *
     * @param userId the user ID
     * @return {@code true} if logged in
     */
    public static synchronized boolean isLoggedIn(Long userId) {
        return sessions.containsKey(userId);
    }
}