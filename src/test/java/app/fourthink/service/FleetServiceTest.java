package app.fourthink.service;

import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.persistence.CabModelRepository;
import app.fourthink.persistence.CabRepository;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class FleetServiceTest {

    @Autowired
    private FleetService fleet;

    @Autowired
    private CabModelRepository models;

    @Autowired
    private CabRepository cabs;

    private CabModel anyModel;

    @Before
    public void setUp() {
        anyModel = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL));
    }

    @Test
    public void registersACab() {
        Cab cab = fleet.register("ABC-1234", anyModel.getId());
        assertNotNull(cab.getId());
        assertEquals(CabStatus.OFFLINE, cab.getStatus());
    }

    @Test
    public void rejectsDuplicatePlate() {
        fleet.register("ABC-1234", anyModel.getId());
        try {
            fleet.register("ABC1234", anyModel.getId());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void rejectsUnknownModel() {
        try {
            fleet.register("ABC-1234", 999L);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void changesStatus() {
        Cab cab = fleet.register("ABC-1234", anyModel.getId());
        fleet.updateStatus(cab.getId(), CabStatus.FREE);
        assertEquals(CabStatus.FREE, fleet.get(cab.getId()).getStatus());
    }

    @Test
    public void updatesLocation() {
        Cab cab = fleet.register("ABC-1234", anyModel.getId());
        fleet.updateLocation(cab.getId(), -30.0, -51.0);
        assertEquals(-30.0, fleet.get(cab.getId()).getLocation().getLatitude(), 0.0);
    }

    @Test
    public void deregisters() {
        Cab cab = fleet.register("ABC-1234", anyModel.getId());
        fleet.deregister(cab.getId());
        assertNull(cabs.findById(cab.getId()));
    }
}
