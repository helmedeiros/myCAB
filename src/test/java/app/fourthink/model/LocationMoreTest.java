package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationMoreTest {

    @Test
    public void acceptsNorthPole() {
        assertEquals(90.0, new Location(90.0, 0.0).getLatitude(), 0.0);
    }

    @Test
    public void acceptsAntimeridian() {
        assertEquals(180.0, new Location(0.0, 180.0).getLongitude(), 0.0);
    }
}
