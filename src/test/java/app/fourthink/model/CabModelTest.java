package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CabModelTest {

    @Test
    public void storesMakeModelAndCategory() {
        CabModel m = new CabModel("Volkswagen", "Gol", CabCategory.NORMAL);
        assertEquals("Volkswagen", m.getMake());
        assertEquals("Gol", m.getModel());
        assertEquals(CabCategory.NORMAL, m.getCategory());
    }

    @Test
    public void newInstanceHasNoIdentity() {
        CabModel m = new CabModel("Fiat", "Palio", CabCategory.NORMAL);
        assertNull(m.getId());
    }

    @Test
    public void displayNameCombinesMakeAndModel() {
        CabModel m = new CabModel("Toyota", "Corolla", CabCategory.CONFORTO);
        assertEquals("Toyota Corolla", m.getDisplayName());
    }
}
