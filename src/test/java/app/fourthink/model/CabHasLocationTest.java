package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CabHasLocationTest {

    private final CabModel anyModel = new CabModel("Volkswagen", "Gol", CabCategory.NORMAL);

    @Test
    public void freshCabHasNoLocation() {
        assertFalse(new Cab(new Plate("ABC-1234"), anyModel).hasLocation());
    }

    @Test
    public void cabWithCoordsHasLocation() {
        Cab cab = new Cab(new Plate("ABC-1234"), anyModel);
        cab.updateLocation(new Location(-30.0, -51.0));
        assertTrue(cab.hasLocation());
    }
}
