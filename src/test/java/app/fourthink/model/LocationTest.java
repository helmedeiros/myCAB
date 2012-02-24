package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LocationTest {

    @Test
    public void exposesCoordinates() {
        Location l = new Location(-30.0277, -51.2287);
        assertEquals(-30.0277, l.getLatitude(), 0.0);
        assertEquals(-51.2287, l.getLongitude(), 0.0);
    }

    @Test
    public void rejectsLatitudeAboveNinety() {
        try {
            new Location(91.0, 0.0);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("latitude"));
        }
    }

    @Test
    public void rejectsLatitudeBelowMinusNinety() {
        try {
            new Location(-91.0, 0.0);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("latitude"));
        }
    }

    @Test
    public void rejectsLongitudeAboveOneEighty() {
        try {
            new Location(0.0, 181.0);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("longitude"));
        }
    }

    @Test
    public void rejectsLongitudeBelowMinusOneEighty() {
        try {
            new Location(0.0, -181.0);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("longitude"));
        }
    }

    @Test
    public void sameCoordinatesAreEqual() {
        assertEquals(new Location(1.0, 2.0), new Location(1.0, 2.0));
    }

    @Test
    public void differentCoordinatesAreNotEqual() {
        assertFalse(new Location(1.0, 2.0).equals(new Location(1.0, 3.0)));
    }
}
