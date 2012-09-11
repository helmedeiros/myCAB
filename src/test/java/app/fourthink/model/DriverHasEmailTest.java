package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DriverHasEmailTest {

    @Test
    public void matchesIgnoringCase() {
        Driver d = new Driver("Jose", "jose@example.com", new Phone("(51) 99999-0000"),
                "ABC123456", "hash$x", CabCategory.NORMAL);
        assertTrue(d.hasEmail("JOSE@example.com"));
    }

    @Test
    public void rejectsDifferent() {
        Driver d = new Driver("Jose", "jose@example.com", new Phone("(51) 99999-0000"),
                "ABC123456", "hash$x", CabCategory.NORMAL);
        assertFalse(d.hasEmail("other@example.com"));
    }

    @Test
    public void rejectsNull() {
        Driver d = new Driver("Jose", "jose@example.com", new Phone("(51) 99999-0000"),
                "ABC123456", "hash$x", CabCategory.NORMAL);
        assertFalse(d.hasEmail(null));
    }
}
