package kim.donghyun.config;


import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import kim.donghyun.model.entity.User;
import kim.donghyun.util.LoginSessionManager;

/**
 * Listener that removes a user from the {@link LoginSessionManager} when their
 * session is destroyed.
 */
public class SessionCleanupListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Object obj = se.getSession().getAttribute("loginUser");
        if (obj instanceof User user) {
            LoginSessionManager.removeUser(user.getId());
        }
    }
}