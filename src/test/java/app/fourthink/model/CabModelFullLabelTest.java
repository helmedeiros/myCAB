package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CabModelFullLabelTest {

    @Test
    public void labelHasMakeModelAndCategory() {
        CabModel m = new CabModel("Toyota", "Corolla", CabCategory.CONFORTO);
        assertEquals("Toyota Corolla (Conforto)", m.fullLabel());
    }
}
