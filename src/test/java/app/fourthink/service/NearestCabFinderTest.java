package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Location;
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
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class NearestCabFinderTest {

    @Autowired
    private NearestCabFinder finder;

    @Autowired
    private FleetService fleet;

    @Autowired
    private CabModelRepository models;

    private Long normalModel;
    private Long confortoModel;

    @Before
    public void setUp() {
        normalModel = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL)).getId();
        confortoModel = models.save(new CabModel("Toyota", "Corolla", CabCategory.CONFORTO)).getId();
    }

    @Test
    public void returnsNullWhenNoCabAvailable() {
        assertNull(finder.findClosest(new Location(-30.0, -51.0), CabCategory.NORMAL));
    }

    @Test
    public void picksClosestFreeOfCategory() {
        registerFree("ABC-1111", normalModel, -30.001, -51.001);
        registerFree("DEF-2222", normalModel, -30.500, -51.500);
        assertEquals("ABC-1111", finder.findClosest(new Location(-30.0, -51.0), CabCategory.NORMAL)
                .getPlate().getValue());
    }

    @Test
    public void ignoresOtherCategories() {
        registerFree("ABC-1111", confortoModel, -30.001, -51.001);
        assertNull(finder.findClosest(new Location(-30.0, -51.0), CabCategory.NORMAL));
    }

    @Test
    public void ignoresBusyCabs() {
        Long id = registerFree("ABC-1111", normalModel, -30.001, -51.001);
        fleet.updateStatus(id, CabStatus.BUSY);
        assertNull(finder.findClosest(new Location(-30.0, -51.0), CabCategory.NORMAL));
    }

    private Long registerFree(String plate, Long modelId, double lat, double lon) {
        Long id = fleet.register(plate, modelId).getId();
        fleet.updateLocation(id, lat, lon);
        fleet.updateStatus(id, CabStatus.FREE);
        return id;
    }
}
