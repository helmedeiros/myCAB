package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CabTest {

    private final CabModel anyModel = new CabModel("Volkswagen", "Gol", CabCategory.NORMAL);

    @Test
    public void newCabIsOffline() {
        Cab cab = new Cab(new Plate("ABC-1234"), anyModel);
        assertEquals(CabStatus.OFFLINE, cab.getStatus());
    }

    @Test
    public void cabHasNoLocationUntilUpdated() {
        Cab cab = new Cab(new Plate("ABC-1234"), anyModel);
        assertNull(cab.getLocation());
    }

    @Test
    public void updateLocationStoresCoordinates() {
        Cab cab = new Cab(new Plate("ABC-1234"), anyModel);
        cab.updateLocation(new Location(-30.0, -51.0));
        assertEquals(new Location(-30.0, -51.0), cab.getLocation());
    }

    @Test
    public void statusCanFlipToFree() {
        Cab cab = new Cab(new Plate("ABC-1234"), anyModel);
        cab.setStatus(CabStatus.FREE);
        assertEquals(CabStatus.FREE, cab.getStatus());
    }
}
