package app.fourthink.persistence;

import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Location;
import app.fourthink.model.Plate;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class JpaCabRepositoryTest {

    @Autowired
    private CabRepository cabs;

    @Autowired
    private CabModelRepository models;

    private CabModel model;

    @Before
    public void setUp() {
        model = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL));
    }

    @Test
    public void persistsAndRetrievesCab() {
        Cab saved = cabs.save(new Cab(new Plate("ABC-1234"), model));
        assertNotNull(saved.getId());
        Cab found = cabs.findById(saved.getId());
        assertEquals("ABC-1234", found.getPlate().getValue());
    }

    @Test
    public void findsByPlate() {
        cabs.save(new Cab(new Plate("DEF-9999"), model));
        Cab found = cabs.findByPlate("DEF-9999");
        assertNotNull(found);
    }

    @Test
    public void findsByStatus() {
        Cab cab = cabs.save(new Cab(new Plate("GHI-1111"), model));
        cab.setStatus(CabStatus.FREE);
        cabs.save(cab);
        assertEquals(1, cabs.findByStatus(CabStatus.FREE).size());
    }

    @Test
    public void deletesCab() {
        Cab cab = cabs.save(new Cab(new Plate("JKL-2222"), model));
        cabs.delete(cab);
        assertNull(cabs.findById(cab.getId()));
    }

    @Test
    public void persistsLocation() {
        Cab cab = new Cab(new Plate("MNO-3333"), model);
        cab.updateLocation(new Location(-30.0, -51.0));
        cabs.save(cab);
        Cab reloaded = cabs.findByPlate("MNO-3333");
        assertEquals(new Location(-30.0, -51.0), reloaded.getLocation());
    }
}
