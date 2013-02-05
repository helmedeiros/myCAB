package app.fourthink.security;

import javax.servlet.http.HttpSession;

public final class SessionGate {

    public static final String DRIVER_ID = "driverId";
    public static final String CUSTOMER_ID = "customerId";

    private SessionGate() {}

    public static boolean isAuthenticated(HttpSession session) {
        return session != null && session.getAttribute(DRIVER_ID) != null;
    }

    public static Long driverId(HttpSession session) {
        Object id = session.getAttribute(DRIVER_ID);
        return id instanceof Long ? (Long) id : null;
    }

    public static boolean isCustomerAuthenticated(HttpSession session) {
        return session != null && session.getAttribute(CUSTOMER_ID) != null;
    }

    public static Long customerId(HttpSession session) {
        Object id = session.getAttribute(CUSTOMER_ID);
        return id instanceof Long ? (Long) id : null;
    }
}
