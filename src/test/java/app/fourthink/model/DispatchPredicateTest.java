package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DispatchPredicateTest {

    private final CabModel anyModel = new CabModel("Volkswagen", "Gol", CabCategory.NORMAL);
    private final Customer anyCustomer = new Customer("Maria", new Phone("(51) 99999-1234"), null);
    private final Location pickup = new Location(-30.0, -51.0);

    @Test
    public void newDispatchIsOpenButNotAssigned() {
        Dispatch d = new Dispatch(anyCustomer, pickup, "Rua A", CabCategory.NORMAL);
        assertTrue(d.isOpen());
        assertFalse(d.isAssigned());
    }

    @Test
    public void assignedDispatchIsAssignedAndOpen() {
        Dispatch d = new Dispatch(anyCustomer, pickup, "Rua A", CabCategory.NORMAL);
        d.assignTo(new Cab(new Plate("ABC-1234"), anyModel));
        assertTrue(d.isOpen());
        assertTrue(d.isAssigned());
    }

    @Test
    public void completedDispatchIsClosed() {
        Dispatch d = new Dispatch(anyCustomer, pickup, "Rua A", CabCategory.NORMAL);
        d.assignTo(new Cab(new Plate("ABC-1234"), anyModel));
        d.complete();
        assertFalse(d.isOpen());
    }
}
