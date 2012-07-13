package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DistanceMoreTest {

    @Test
    public void closeBlocksAreUnderTwoKilometers() {
        Location a = new Location(-30.0277, -51.2287);
        Location b = new Location(-30.0290, -51.2290);
        double km = Distance.kilometers(a, b);
        assertTrue(km < 2.0);
    }
}
