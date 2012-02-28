package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DistanceTest {

    @Test
    public void samePointIsZero() {
        Location p = new Location(-30.0277, -51.2287);
        assertEquals(0.0, Distance.kilometers(p, p), 0.0001);
    }

    @Test
    public void portoAlegreToSaoPauloIsAboutOneThousandKilometers() {
        Location poa = new Location(-30.0277, -51.2287);
        Location sao = new Location(-23.5505, -46.6333);
        double km = Distance.kilometers(poa, sao);
        assertTrue("expected ~850-900km but was " + km, km > 800 && km < 950);
    }

    @Test
    public void isSymmetric() {
        Location a = new Location(-30.0, -51.0);
        Location b = new Location(-23.0, -46.0);
        assertEquals(Distance.kilometers(a, b), Distance.kilometers(b, a), 0.0001);
    }
}
