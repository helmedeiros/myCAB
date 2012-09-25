package app.fourthink.security;

import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SessionGateTest {

    @Test
    public void newSessionIsNotAuthenticated() {
        assertFalse(SessionGate.isAuthenticated(new MockHttpSession()));
    }

    @Test
    public void sessionWithDriverIdIsAuthenticated() {
        MockHttpSession s = new MockHttpSession();
        s.setAttribute(SessionGate.DRIVER_ID, 7L);
        assertTrue(SessionGate.isAuthenticated(s));
        assertEquals(Long.valueOf(7L), SessionGate.driverId(s));
    }

    @Test
    public void nullSessionIsNotAuthenticated() {
        assertFalse(SessionGate.isAuthenticated(null));
    }
}
