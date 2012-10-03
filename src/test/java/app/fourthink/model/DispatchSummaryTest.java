package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DispatchSummaryTest {

    @Test
    public void summarizesUnassignedDispatch() {
        Customer maria = new Customer("Maria", new Phone("(51) 99999-1234"), null);
        Dispatch d = new Dispatch(maria, new Location(-30.0, -51.0), "Rua A", CabCategory.NORMAL);
        DispatchSummary s = DispatchSummary.of(d);
        assertEquals("Maria", s.getCustomerName());
        assertEquals(DispatchStatus.REQUESTED, s.getStatus());
        assertNull(s.getAssignedPlate());
    }

    @Test
    public void summarizesAssignedDispatch() {
        Customer maria = new Customer("Maria", new Phone("(51) 99999-1234"), null);
        Dispatch d = new Dispatch(maria, new Location(-30.0, -51.0), "Rua A", CabCategory.NORMAL);
        CabModel model = new CabModel("Volkswagen", "Gol", CabCategory.NORMAL);
        d.assignTo(new Cab(new Plate("ABC-1234"), model));
        assertEquals("ABC-1234", DispatchSummary.of(d).getAssignedPlate());
    }
}
