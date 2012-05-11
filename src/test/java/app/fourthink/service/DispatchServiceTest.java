package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Customer;
import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import app.fourthink.model.Phone;
import app.fourthink.persistence.CabModelRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class DispatchServiceTest {

    @Autowired
    private DispatchService dispatch;

    @Autowired
    private FleetService fleet;

    @Autowired
    private CustomerService customers;

    @Autowired
    private CabModelRepository models;

    private CabModel anyModel;
    private Customer maria;

    @Before
    public void setUp() {
        anyModel = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL));
        maria = customers.register("Maria Silva", "(51) 99999-1234", "Rua A");
    }

    @Test
    public void createsRequestedDispatch() {
        Dispatch d = dispatch.request(maria.getId(), -30.0, -51.0, "Rua A", CabCategory.NORMAL);
        assertEquals(DispatchStatus.REQUESTED, d.getStatus());
    }

    @Test
    public void assignsClosestFreeCab() {
        registerFreeCab("ABC-1234", -30.0, -51.0);
        registerFreeCab("DEF-5678", -31.0, -52.0);
        Dispatch d = dispatch.request(maria.getId(), -30.0001, -51.0001, "Rua A", CabCategory.NORMAL);
        Dispatch assigned = dispatch.assignClosest(d.getId());
        assertEquals("ABC-1234", assigned.getAssignedCab().getPlate().getValue());
        assertEquals(DispatchStatus.ASSIGNED, assigned.getStatus());
        assertEquals(CabStatus.BUSY, assigned.getAssignedCab().getStatus());
    }

    @Test
    public void failsWhenNoCabAvailable() {
        Dispatch d = dispatch.request(maria.getId(), -30.0, -51.0, "Rua A", CabCategory.NORMAL);
        try {
            dispatch.assignClosest(d.getId());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void completingFreesTheCab() {
        registerFreeCab("ABC-1234", -30.0, -51.0);
        Dispatch d = dispatch.request(maria.getId(), -30.0, -51.0, "Rua A", CabCategory.NORMAL);
        Dispatch assigned = dispatch.assignClosest(d.getId());
        Dispatch done = dispatch.complete(assigned.getId());
        assertEquals(DispatchStatus.COMPLETED, done.getStatus());
        assertEquals(CabStatus.FREE, fleet.get(done.getAssignedCab().getId()).getStatus());
    }

    private void registerFreeCab(String plate, double lat, double lon) {
        Long id = fleet.register(plate, anyModel.getId()).getId();
        fleet.updateLocation(id, lat, lon);
        fleet.updateStatus(id, CabStatus.FREE);
        assertNotNull(fleet.get(id));
    }
}
