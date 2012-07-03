package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DispatchTest {

    private final CabModel anyModel = new CabModel("Volkswagen", "Gol", CabCategory.NORMAL);
    private final Customer anyCustomer = new Customer("Maria", new Phone("(51) 99999-1234"), null);
    private final Location pickup = new Location(-30.0, -51.0);

    @Test
    public void newDispatchIsRequested() {
        Dispatch d = new Dispatch(anyCustomer, pickup, "Rua A", CabCategory.NORMAL);
        assertEquals(DispatchStatus.REQUESTED, d.getStatus());
    }

    @Test
    public void assigningChangesStateAndAttachesCab() {
        Dispatch d = new Dispatch(anyCustomer, pickup, "Rua A", CabCategory.NORMAL);
        Cab cab = new Cab(new Plate("ABC-1234"), anyModel);
        d.assignTo(cab);
        assertEquals(DispatchStatus.ASSIGNED, d.getStatus());
        assertEquals(cab, d.getAssignedCab());
    }

    @Test
    public void cannotAssignTwice() {
        Dispatch d = new Dispatch(anyCustomer, pickup, "Rua A", CabCategory.NORMAL);
        d.assignTo(new Cab(new Plate("ABC-1234"), anyModel));
        try {
            d.assignTo(new Cab(new Plate("DEF-5678"), anyModel));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void completedDispatchCannotBeCancelled() {
        Dispatch d = new Dispatch(anyCustomer, pickup, "Rua A", CabCategory.NORMAL);
        d.assignTo(new Cab(new Plate("ABC-1234"), anyModel));
        d.complete();
        try {
            d.cancel();
            fail();
        } catch (IllegalStateException expected) {
        }
    }
}
