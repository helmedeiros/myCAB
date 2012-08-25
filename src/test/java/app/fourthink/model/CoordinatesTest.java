package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoordinatesTest {

    @Test
    public void convertsToLocation() {
        Coordinates c = new Coordinates(-30.0, -51.0);
        assertEquals(new Location(-30.0, -51.0), c.toLocation());
    }

    @Test
    public void buildsFromLocation() {
        Coordinates c = Coordinates.of(new Location(-30.0, -51.0));
        assertEquals(-30.0, c.getLatitude(), 0.0);
    }
}
