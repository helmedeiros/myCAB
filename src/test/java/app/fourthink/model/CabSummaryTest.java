package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CabSummaryTest {

    @Test
    public void summarizesACab() {
        CabModel model = new CabModel("Volkswagen", "Gol", CabCategory.NORMAL);
        Cab cab = new Cab(new Plate("ABC-1234"), model);
        CabSummary s = CabSummary.of(cab);
        assertEquals("ABC-1234", s.getPlate());
        assertEquals("Volkswagen Gol", s.getModelDisplay());
        assertEquals(CabCategory.NORMAL, s.getCategory());
        assertEquals(CabStatus.OFFLINE, s.getStatus());
    }
}
