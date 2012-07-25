package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DriverProfileTest {

    @Test
    public void projectsFromDriver() {
        Driver d = new Driver("Jose", "jose@example.com", new Phone("(51) 99999-0000"),
                "ABC123456", "hash$x", CabCategory.NORMAL);
        DriverProfile p = DriverProfile.of(d);
        assertEquals("Jose", p.getFullName());
        assertEquals("jose@example.com", p.getEmail());
        assertEquals("(51) 99999-0000", p.getPhone());
    }
}
