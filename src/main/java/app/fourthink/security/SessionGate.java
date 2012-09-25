package app.fourthink.security;

import javax.servlet.http.HttpSession;

public final class SessionGate {

    public static final String DRIVER_ID = "driverId";

    private SessionGate() {}

    public static boolean isAuthenticated(HttpSession session) {
        return session != null && session.getAttribute(DRIVER_ID) != null;
    }

    public static Long driverId(HttpSession session) {
        Object id = session.getAttribute(DRIVER_ID);
        return id instanceof Long ? (Long) id : null;
    }
}
